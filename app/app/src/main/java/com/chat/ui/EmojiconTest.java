package com.chat.ui;

import android.app.FragmentManager;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import com.chat.mobile.R;
import com.chat.util.Ln;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

public class EmojiconTest extends FragmentActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    EmojiconEditText mEditEmojicon;
    EmojiconTextView mTxtEmojicon;
    public Fragment emojicons;
    private InputMethodManager manager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emojicon_test);
        mEditEmojicon = (EmojiconEditText) findViewById(R.id.editEmojicon);
        mTxtEmojicon = (EmojiconTextView) findViewById(R.id.txtEmojicon);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditEmojicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideEmojicons();
            }
        });
        mEditEmojicon.addTextChangedListener(new TextWatcher() {
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
        emojicons = getSupportFragmentManager().findFragmentById(R.id.emojicons);
        if (emojicons != null) {
            hideEmojicons();
        }
        ImageView btnShowEmojicons = (ImageView) findViewById(R.id.iv_emoticons_normal);
        btnShowEmojicons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emojicons.isHidden()) {
                    showEmojicons();
                } else {
                    hideEmojicons();
                }
            }
        });
        initView();
    }

    private void showEmojicons() {
        getSupportFragmentManager().beginTransaction().show(emojicons).commit();
        hideKeyboard();
    }

    private void hideEmojicons() {
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

    private void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(mEditEmojicon, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(mEditEmojicon);
    }
}
