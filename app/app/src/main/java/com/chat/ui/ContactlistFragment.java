package com.chat.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.chat.core.ChatService;
import com.chat.mobile.R;
import com.chat.ui.model.User;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ContactlistFragment extends Fragment {
    private Context context;
    private View view;
    private List<User> contactList;
    @Inject
    public ChatService chatService;
    @InjectView(R.id.list)
    public ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        }
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeAllViewsInLayout();
        }
        context = getActivity();
        ButterKnife.inject(this, view);
        initView();
        EMContactManager.getInstance().setContactListener(new EMContactListener() {

            @Override
            public void onContactAgreed(String username) {
                Toast.makeText(getActivity(), "onContactAgreed " + username, Toast.LENGTH_LONG).show();
                //好友请求被同意
            }

            @Override
            public void onContactRefused(String username) {
                Toast.makeText(getActivity(), "onContactRefused " + username, Toast.LENGTH_LONG).show();
                //好友请求被拒绝
            }

            @Override
            public void onContactInvited(String username, String reason) {
                Toast.makeText(getActivity(), "onContactInvited " + username, Toast.LENGTH_LONG).show();
                //收到好友邀请
            }

            @Override
            public void onContactDeleted(List<String> usernameList) {
                Toast.makeText(getActivity(), "onContactDeleted", Toast.LENGTH_LONG).show();
                //被删除时回调此方法
            }


            @Override
            public void onContactAdded(List<String> usernameList) {
                Toast.makeText(getActivity(), "onContactAdded", Toast.LENGTH_LONG).show();
                //增加了联系人时回调此方法
            }
        });
        return view;
    }

    /**
     * 获取联系人列表，并过滤掉黑名单和排序
     */
    private List<String> getContactList() {
        List<String> usernames;
        try {
            usernames = EMContactManager.getInstance().getContactUserNames();
        } catch (Exception ex) {
            ex.printStackTrace();
            usernames = new ArrayList<String>();
        }
        if (usernames.size() == 0) {
            usernames.add("tommy123456");
            usernames.add("tommy654321");
        }
        return usernames;
    }

    private void initView() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,
                android.R.id.text1, getContactList());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String userId = adapter.getItem(i);
                Intent intent = new Intent(getActivity(), SingleChatFragment.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.iv_new_contact)
    public void addContact(View view) {
        Intent intent = new Intent(getActivity(), AddContactFragment.class);
        startActivity(intent);
    }
}
