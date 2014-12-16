package com.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chat.ChatApplication;
import com.chat.mobile.R;
import com.chat.ui.adapter.NotificationListAdapter;
import com.chat.ui.model.NotificationRowModel;
import com.chat.util.Ln;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NotificationListFragment extends Fragment {
    private View view;
    @InjectView(R.id.list)
    public ListView list;
    private NotificationListAdapter adapter;
    private List<NotificationRowModel> rowModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        }
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeAllViewsInLayout();
        }
        ButterKnife.inject(this, view);
        rowModels = loadConversationsWithRecentChat();
        adapter = new NotificationListAdapter(getActivity(), 1, rowModels);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NotificationRowModel emContact = adapter.getItem(i);
                String userName=ChatApplication.getInstance().getUserName();
                if (adapter.getItem(i).getName().equals(userName))
                    Toast.makeText(getActivity(), "不能和自己聊天", Toast.LENGTH_SHORT).show();
                else {
                    // 进入聊天页面
                    Intent intent;
                    if (emContact.isGroup()) {
                        //it is group chat
                        intent = new Intent(getActivity(), GroupChatFragment.class);
                        intent.putExtra("chatType", "group");
                        intent.putExtra("groupId", (emContact.getEmGroup()).getGroupId());
                    } else {
                        //it is single chat
                        intent = new Intent(getActivity(), SingleChatFragment.class);
                        intent.putExtra("userId", emContact.getName());
                    }
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    /**
     * 获取所有会话
     *
     * @return 返回需要显示的数据
     */
    private List<NotificationRowModel> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        List<NotificationRowModel> list = new ArrayList<NotificationRowModel>();
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
        for (EMConversation conversation : conversations.values()) {
            if (conversation.getAllMessages().size() != 0) {
                NotificationRowModel messageRow = new NotificationRowModel();
                messageRow.setName(conversation.getUserName());
                messageRow.setMsgCount(conversation.getMsgCount());
                messageRow.setUnreadMsgCount(conversation.getUnreadMsgCount());
                messageRow.setLastMessage(conversation.getLastMessage());
                for (EMGroup group : groups) {
                    if (group.getGroupId().equals(messageRow.getName())) {
                        messageRow.setEmGroup(group);
                        messageRow.setGroup(true);
                        break;
                    }
                }
                list.add(messageRow);
            }
        }
        //test
        if (list.size() == 0) {
            NotificationRowModel messageRow = new NotificationRowModel();
            messageRow.setName("tommy1234567");
            messageRow.setMsgCount(1);
            messageRow.setUnreadMsgCount(1);
            EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            //如果是群聊，设置chattype,默认是单聊
            message.setChatType(EMMessage.ChatType.Chat);
            //设置消息body
            TextMessageBody txtBody = new TextMessageBody("测试数据");
            message.addBody(txtBody);
            //设置接收人
            try {
                message.setReceipt("tommy1234567");
                messageRow.setLastMessage(message);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            list.add(messageRow);
        }
        sortMessageRowModelByLastChatTime(list);
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     *
     * @param messageRowModelList MessageRowModel数组
     */
    private void sortMessageRowModelByLastChatTime(List<NotificationRowModel> messageRowModelList) {
        Collections.sort(messageRowModelList, new Comparator<NotificationRowModel>() {
            @Override
            public int compare(final NotificationRowModel con1, final NotificationRowModel con2) {
                EMMessage con2LastMessage = con2.getLastMessage();
                EMMessage con1LastMessage = con1.getLastMessage();
                if (con2LastMessage.getMsgTime() == con1LastMessage.getMsgTime()) {
                    return 0;
                } else if (con2LastMessage.getMsgTime() > con1LastMessage.getMsgTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }
}
