package com.chat.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.chat.mobile.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MessageFragment extends Fragment {
    private View view;
    @InjectView(R.id.weixin_swipeContainer)
    protected SwipeRefreshLayout swipeContainer;
    @InjectView(R.id.message_listView)
    protected ListView messageListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main, container, false);
        }
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeAllViewsInLayout();
        }
        ButterKnife.inject(this, view);
        return view;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setTop(20);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeContainer.setRefreshing(false);
                    }
                }, 5000);
            }
        });
        ArrayList<String> listData = new ArrayList<String>();
        listData.add("123");
        listData.add("456");
        listData.add("789");
        listData.add("321");
        listData.add("123");
        listData.add("456");
        listData.add("789");
        listData.add("321");
        listData.add("123");
        listData.add("456");
        listData.add("789");
        listData.add("321");
        listData.add("123");
        listData.add("456");
        listData.add("789");
        listData.add("321");
        listData.add("123");
        listData.add("456");
        listData.add("789");
        listData.add("321");
        listData.add("123");
        listData.add("456");
        listData.add("789");
        listData.add("321");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,listData);
        messageListView.setAdapter(adapter);
    }
}
