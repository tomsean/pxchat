package com.chat.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chat.mobile.R;
import com.chat.util.Ln;
import com.rockerhieu.emojicon.EmojiconsFragment;

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
        TextView title = (TextView) v.findViewById(R.id.fragment_me_title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EmojiconTest.class);
                try {
                    startActivity(intent);
                }catch (Exception ex){
                    Ln.e(ex.getMessage());
                }
            }
        });
        context = getActivity();
        initView();
        return v;

    }

    private void initView() {
        // TODO Auto-generated method stub

    }
}
