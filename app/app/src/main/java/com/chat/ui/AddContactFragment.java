package com.chat.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chat.mobile.R;
import com.easemob.chat.EMContactManager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddContactFragment extends FragmentActivity {
    @InjectView(R.id.edit_note)
    public EditText editNote;
    @InjectView(R.id.name)
    public TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_contact);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.search)
    public void searchUser(View view) {
        String userId = editNote.getText().toString();
        name.setText(userId);
    }

    @OnClick(R.id.indicator)
    public void add(View view) {
        String userId = name.getText().toString();
        try {
            EMContactManager.getInstance().addContact(userId, "你好我想加你为好友，测试");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void back(View view) {
        finish();
    }
}
