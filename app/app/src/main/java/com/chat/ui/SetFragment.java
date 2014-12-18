package com.chat.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.mobile.R;
import com.easemob.chat.EMChatManager;
import com.squareup.picasso.Picasso;

public class SetFragment extends Fragment {
    private Context context;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_me, container, false);
        }
        ViewGroup viewGroup = (ViewGroup) v.getParent();
        if (viewGroup != null) {
            viewGroup.removeAllViewsInLayout();
        }
        context = getActivity();

        initView();
        return v;

    }

    private void initView() {
        // TODO Auto-generated method stub
        TextView title = (TextView) v.findViewById(R.id.title2);

        try {
            String userName = EMChatManager.getInstance().getCurrentUser();
            title.setText(userName);
        } catch (Exception ex) {

        }
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getActivity(), LoginFragment.class);
                    startActivity(intent);
                } catch (Exception ex) {

                }
            }
        });
    }
}
