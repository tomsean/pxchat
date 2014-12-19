package com.chat.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chat.mobile.R;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

public class LoginFragment extends FragmentActivity {
    @InjectView(R.id.userName)
    public EditText userName;
    @InjectView(R.id.pwd)
    public EditText pwd;
    private Activity content;
   private PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        ImageView imgTest = (ImageView)findViewById(R.id.img_test);
        try {
            Picasso.with(this)
                    .load("http://www.qqzhi.com/uploadpic/2014-05-07/220325164.jpg")
                    .error(R.drawable.ee_1).into(imgTest);
            mAttacher = new PhotoViewAttacher(imgTest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ButterKnife.inject(this);
        content = this;
    }

    @OnClick(R.id.btn_login)
    public void LoginClick(View view) {
        if (EMChatManager.getInstance().isConnected()) {
            EMChatManager.getInstance().logout();
        }
        EMChatManager.getInstance().login(userName.getText().toString(), pwd.getText().toString(), new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                /*try {
                    if (userName.getText().toString() == "tommy123456") {
                        EMContactManager.getInstance().addContact("tommy654321", "sadf");
                    } else {
                        EMChatManager.getInstance().acceptInvitation("tommy123456");
                    }
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }*/
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
            }
        });
    }

    @OnClick(R.id.btn_register)
    public void Register(View view) {
        final String userNameStr = userName.getText().toString();
        final String pwdStr = pwd.getText().toString();
        if (EMChatManager.getInstance().isConnected()) {
            EMChatManager.getInstance().logout();
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    // 调用sdk注册方法
                    EMChatManager.getInstance().createAccountOnServer(userNameStr, pwdStr);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
