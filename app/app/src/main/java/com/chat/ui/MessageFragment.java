package com.chat.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chat.mobile.R;
import com.chat.ui.adapter.MessageAdapter;
import com.chat.ui.model.MessageRowModel;
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

public class MessageFragment extends SwipeRefreshListFragment {
    private List<MessageRowModel> messageRowModels;
    private MessageAdapter adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setColorScheme(R.color.color_refresh_3_1, R.color.color_refresh_3_2,
                R.color.color_refresh_3_3, R.color.color_refresh_3_4);
        ListView listView = getListView();
        messageRowModels = loadConversationsWithRecentChat();
        if (listView.getHeaderViewsCount() == 0) {
            View header = View.inflate(this.getActivity(), R.layout.activity_main_head, null);
            listView.addHeaderView(header);
        }
        adapter = new MessageAdapter(getActivity(), 1, messageRowModels);
        setListAdapter(adapter);
        /**
         * Implement {@link android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener}. When users do the "swipe to
         * refresh" gesture, SwipeRefreshLayout invokes
         * {@link android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener#onRefresh onRefresh()}. In
         * {@link android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener#onRefresh onRefresh()}, call a method that
         * refreshes the content. Call the same method in response to the Refresh action from the
         * action bar.
         */
        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Ln.i("微信", "onRefresh called from SwipeRefreshLayout");
                initiateRefresh();
            }
        });
        ButterKnife.inject(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        setListAdapter(null);
    }

    private void initiateRefresh() {
        Ln.i("微信", "initiateRefresh");

        /**
         * Execute the background task, which uses {@link android.os.AsyncTask} to load the data.
         */
        new DummyBackgroundTask().execute();
    }

    private void onRefreshComplete(List<String> result) {
        Ln.i("微信", "onRefreshComplete");
        messageRowModels.clear();
        messageRowModels.addAll(loadConversationsWithRecentChat());
        adapter.notifyDataSetChanged();
        // Stop the refreshing indicator
        setRefreshing(false);
    }

    private class DummyBackgroundTask extends AsyncTask<Void, Void, List<String>> {

        static final int TASK_DURATION = 3 * 1000;

        @Override
        protected List<String> doInBackground(Void... params) {
            // Sleep for a small amount of time to simulate a background-task
            try {
                Thread.sleep(TASK_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ArrayList<String> newData = new ArrayList<String>();
            return newData;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);

            // Tell the Fragment that the refresh has completed
            onRefreshComplete(result);
        }

    }

    /**
     * 获取所有会话
     *
     * @return 返回需要显示的数据
     */
    private List<MessageRowModel> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        List<MessageRowModel> list = new ArrayList<MessageRowModel>();
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
        for (EMConversation conversation : conversations.values()) {
            if (conversation.getAllMessages().size() != 0) {
                MessageRowModel messageRow = new MessageRowModel();
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
            MessageRowModel messageRow = new MessageRowModel();
            messageRow.setName("123");
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
                message.setReceipt("456");
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
    private void sortMessageRowModelByLastChatTime(List<MessageRowModel> messageRowModelList) {
        Collections.sort(messageRowModelList, new Comparator<MessageRowModel>() {
            @Override
            public int compare(final MessageRowModel con1, final MessageRowModel con2) {
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
