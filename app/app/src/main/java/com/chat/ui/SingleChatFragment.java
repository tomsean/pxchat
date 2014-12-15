package com.chat.ui;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chat.mobile.R;
import com.chat.util.Ln;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SingleChatFragment extends FragmentActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    @InjectView(R.id.et_sendmessage)
    public EmojiconEditText sendMessage;
    public Fragment emojicons;
    @InjectView(R.id.iv_emoticons_normal)
    public ImageView emoticonsNormal;
    @InjectView(R.id.iv_emoticons_checked)
    public ImageView emoticonsChecked;
    @InjectView(R.id.more)
    public LinearLayout more;
    private InputMethodManager manager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_single_chat);
        ButterKnife.inject(this);
//        if (emojicons != null) {
//            hideEmojicons();
//        }
        emojicons = getSupportFragmentManager().findFragmentById(R.id.emojicons);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emoticonsNormal.setVisibility(View.VISIBLE);
                emoticonsChecked.setVisibility(View.INVISIBLE);
                more.setVisibility(View.INVISIBLE);
                hideEmojicons();
            }
        });
        sendMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String str = charSequence.toString();
                Ln.i(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        emoticonsNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emoticonsChecked.setVisibility(View.VISIBLE);
                emoticonsNormal.setVisibility(View.INVISIBLE);
                showEmojicons();
            }
        });
        emoticonsChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emoticonsNormal.setVisibility(View.VISIBLE);
                emoticonsChecked.setVisibility(View.INVISIBLE);
                hideEmojicons();
            }
        });
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(sendMessage, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View view) {
        EmojiconsFragment.backspace(sendMessage);
    }

    private void showEmojicons() {
        hideKeyboard();
        more.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().show(emojicons).commit();
    }

    private void hideEmojicons() {
        more.setVisibility(View.INVISIBLE);
        getSupportFragmentManager().beginTransaction().hide(emojicons).commit();
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void back(View view) {
        finish();
    }
}
