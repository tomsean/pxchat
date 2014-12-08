package com.chat.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.chat.mobile.R;
import com.chat.util.Ln;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class WeixinFragment extends SwipeRefreshListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //        // TODO Auto-generated method stub
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_main, container, false);
        }
        ViewGroup viewGroup = (ViewGroup) v.getParent();
        if (viewGroup != null) {
            viewGroup.removeAllViewsInLayout();
        }
        context = getActivity();
        return v;
    }*/
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setColorScheme(R.color.color_refresh_3_1, R.color.color_refresh_3_2,
                R.color.color_refresh_3_3, R.color.color_refresh_3_4);
        ArrayList<String> listData = new ArrayList<String>();
        ListView listView = getListView();
        if (listView.getHeaderViewsCount() == 0) {
            View header = View.inflate(this.getActivity(), R.layout.activity_main_head, null);
            listView.addHeaderView(header);
        }
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
        ListAdapter adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1, listData);
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

        // Remove all items from the ListAdapter, and then replace them with the new items
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
        adapter.clear();
        for (String item : result) {
            adapter.add(item);
        }

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
            newData.add("new 123");
            newData.add("new 456");
            return newData;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);

            // Tell the Fragment that the refresh has completed
            onRefreshComplete(result);
        }

    }
}
