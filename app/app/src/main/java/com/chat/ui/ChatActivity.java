package com.chat.ui;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.ChatApplication;
import com.chat.mobile.R;
import com.chat.ui.adapter.ExpressionAdapter;
import com.chat.ui.adapter.ExpressionPagerAdapter;
import com.chat.ui.adapter.MessageAdapter;
import com.chat.util.CommonUtils;
import com.chat.util.SmileUtils;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;


public class ChatActivity extends ChatFragmentActivity {
    private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
    public static final int REQUEST_CODE_CONTEXT_MENU = 3;
    private static final int REQUEST_CODE_MAP = 4;
    public static final int REQUEST_CODE_TEXT = 5;
    public static final int REQUEST_CODE_VOICE = 6;
    public static final int REQUEST_CODE_PICTURE = 7;
    public static final int REQUEST_CODE_LOCATION = 8;
    public static final int REQUEST_CODE_NET_DISK = 9;
    public static final int REQUEST_CODE_FILE = 10;
    public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
    public static final int REQUEST_CODE_PICK_VIDEO = 12;
    public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
    public static final int REQUEST_CODE_VIDEO = 14;
    public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
    public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
    public static final int REQUEST_CODE_SEND_USER_CARD = 17;
    public static final int REQUEST_CODE_CAMERA = 18;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
    public static final int REQUEST_CODE_GROUP_DETAIL = 21;
    public static final int REQUEST_CODE_SELECT_VIDEO = 23;
    public static final int REQUEST_CODE_SELECT_FILE = 24;
    public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;
    public static final int RESULT_CODE_FORWARD = 3;
    public static final int RESULT_CODE_OPEN = 4;
    public static final int RESULT_CODE_DWONLOAD = 5;
    public static final int RESULT_CODE_TO_CLOUD = 6;
    public static final int RESULT_CODE_EXIT_GROUP = 7;

    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;

    public static final String COPY_IMAGE = "EASEMOBIMG";
    @InjectView(R.id.recording_container)
    public View recordingContainer;
    @InjectView(R.id.mic_image)
    public ImageView micImage;
    @InjectView(R.id.recording_hint)
    public TextView recordingHint;
    @InjectView(R.id.list)
    public ListView listView;
    @InjectView(R.id.btn_set_mode_keyboard)
    public View buttonSetModeKeyboard;
    @InjectView(R.id.btn_set_mode_voice)
    public View buttonSetModeVoice;
    @InjectView(R.id.btn_send)
    public View buttonSend;
    @InjectView(R.id.btn_press_to_speak)
    public View buttonPressToSpeak;
    @InjectView(R.id.ll_face_container)
    public LinearLayout emojiIconContainer;
    @InjectView(R.id.ll_btn_container)
    public LinearLayout btnContainer;
    @InjectView(R.id.btn_location)
    public ImageView locationImgview;
    @InjectView(R.id.more)
    public View more;
    @InjectView(R.id.iv_emoticons_normal)
    public ImageView iv_emoticons_normal;
    @InjectView(R.id.iv_emoticons_checked)
    public ImageView iv_emoticons_checked;
    @InjectView(R.id.edittext_layout)
    public RelativeLayout edittext_layout;
    @InjectView(R.id.vPager)
    public ViewPager expressionViewpager;
    @InjectView(R.id.btn_more)
    public Button btnMore;
    @InjectView(R.id.et_sendmessage)
    public PasteEditText mEditTextContent;
    private VoiceRecorder voiceRecorder;
    private Drawable[] micImages;
    private List<String> reslist;
    public String playMsgId;
    // 给谁发送消息
    private String toChatUsername;
    private int chatType;
    private EMConversation conversation;
    private MessageAdapter adapter;
    private ClipboardManager clipboard;
    private InputMethodManager manager;
    private EMGroup group;
    private int position;
    public static ChatActivity activityInstance = null;
    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 切换msg切换图片
            micImage.setImageDrawable(micImages[msg.what]);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        setUpView();
    }

    /**
     * initView
     */
    protected void initView() {
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
        // 动画资源文件,用于录制语音时
        micImages = new Drawable[]{getResources().getDrawable(R.drawable.record_animate_01),
                getResources().getDrawable(R.drawable.record_animate_02), getResources().getDrawable(R.drawable.record_animate_03),
                getResources().getDrawable(R.drawable.record_animate_04), getResources().getDrawable(R.drawable.record_animate_05),
                getResources().getDrawable(R.drawable.record_animate_06), getResources().getDrawable(R.drawable.record_animate_07),
                getResources().getDrawable(R.drawable.record_animate_08), getResources().getDrawable(R.drawable.record_animate_09),
                getResources().getDrawable(R.drawable.record_animate_10), getResources().getDrawable(R.drawable.record_animate_11),
                getResources().getDrawable(R.drawable.record_animate_12), getResources().getDrawable(R.drawable.record_animate_13),
                getResources().getDrawable(R.drawable.record_animate_14),};
        // 表情list
        reslist = getExpressionRes(35);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        views.add(gv1);
        views.add(gv2);
        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
        edittext_layout.requestFocus();
        voiceRecorder = new VoiceRecorder(micImageHandler);
        buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
        mEditTextContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
                } else {
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
                }

            }
        });
        mEditTextContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
            }
        });
        // 监听文字框
        mEditTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btnMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    btnMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    protected void setUpView() {
        activityInstance = this;
        position = getIntent().getIntExtra("position", -1);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
        // 判断单聊还是群聊
        chatType = getIntent().getIntExtra("chatType", CHATTYPE_SINGLE);

        if (chatType == CHATTYPE_SINGLE) { // 单聊
            toChatUsername = getIntent().getStringExtra("userId");
            ((TextView) findViewById(R.id.name)).setText(toChatUsername);
            // conversation =
            // EMChatManager.getInstance().getConversation(toChatUsername,false);
        } else {
            // 群聊
            findViewById(R.id.container_to_group).setVisibility(View.VISIBLE);
            findViewById(R.id.container_remove).setVisibility(View.GONE);
            findViewById(R.id.container_voice_call).setVisibility(View.GONE);
            toChatUsername = getIntent().getStringExtra("groupId");
            group = EMGroupManager.getInstance().getGroup(toChatUsername);
            ((TextView) findViewById(R.id.name)).setText(group.getGroupName());
            // conversation =
            // EMChatManager.getInstance().getConversation(toChatUsername,true);
        }
        conversation = EMChatManager.getInstance().getConversation(toChatUsername);
        // 把此会话的未读数置为0
        conversation.resetUnreadMsgCount();
        adapter = new MessageAdapter(this, toChatUsername, chatType);
        // 显示消息
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new ListScrollListener());
        int count = listView.getCount();
        if (count > 0) {
            listView.setSelection(count - 1);
        }

        listView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
                return false;
            }
        });
    }

    /**
     * listview滑动监听listener
     */
    private class ListScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }

    }

    @OnClick(R.id.iv_emoticons_normal)
    public void ivEmoticonsNormalClick(ImageView view) {
        more.setVisibility(View.VISIBLE);
        iv_emoticons_normal.setVisibility(View.INVISIBLE);
        iv_emoticons_checked.setVisibility(View.VISIBLE);
        btnContainer.setVisibility(View.GONE);
        emojiIconContainer.setVisibility(View.VISIBLE);
        hideKeyboard();
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

    /**
     * 点击清空聊天记录
     *
     * @param view
     */
    @OnClick(R.id.container_remove)
    public void emptyHistory(View view) {
        startActivityForResult(
                new Intent(this, AlertDialog.class).putExtra("titleIsCancel", true).putExtra("msg", "是否清空所有聊天记录").putExtra("cancel", true),
                REQUEST_CODE_EMPTY_HISTORY);
    }

    @OnClick(R.id.et_sendmessage)
    public void mEditTextContentClick(View view) {
        listView.setSelection(listView.getCount() - 1);
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.iv_emoticons_checked)
    public void ivEmoticonsCheckedClick(ImageView view) {
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        btnContainer.setVisibility(View.VISIBLE);
        emojiIconContainer.setVisibility(View.GONE);
        more.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_send)
    public void btnSendClick(Button btn) {
        String s = mEditTextContent.getText().toString();
        sendText(s);
    }

    /**
     * 发送文本消息
     *
     * @param content
     */
    private void sendText(String content) {

        if (content.length() > 0) {
            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP)
                message.setChatType(EMMessage.ChatType.GroupChat);
            TextMessageBody txtBody = new TextMessageBody(content);
            // 设置消息body
            message.addBody(txtBody);
            // 设置要发给谁,用户username或者群聊groupid
            message.setReceipt(toChatUsername);
            // 把messgage加到conversation中
            conversation.addMessage(message);
            // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            mEditTextContent.setText("");

            setResult(RESULT_OK);

        }
    }

    @OnClick(R.id.btn_take_picture)
    public void btnTakePictureClick(ImageView image) {

    }

    @OnClick(R.id.btn_picture)
    public void btnPictureClick(ImageView image) {
        Toast.makeText(this, "btn_picture", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_location)
    public void btnLocationClick(ImageView image) {
        Toast.makeText(this, "btn_location", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_video)
    public void btnVideoClick(ImageView image) {
        Toast.makeText(this, "btn_video", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_file)
    public void btnFileClick(ImageView image) {
        Toast.makeText(this, "btn_file", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_voice_call)
    public void btnVoiceCall(ImageView image) {
        Toast.makeText(this, "btn_voice_call", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_more)
    public void btnMoreClick(View view) {
        if (more.getVisibility() == View.GONE) {
            System.out.println("more gone");
            hideKeyboard();
            more.setVisibility(View.VISIBLE);
            btnContainer.setVisibility(View.VISIBLE);
            emojiIconContainer.setVisibility(View.GONE);
        } else {
            if (emojiIconContainer.getVisibility() == View.VISIBLE) {
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情
                    if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {
                        if (filename != "delete_expression") { // 不是删除键，显示表情
                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                            Class clz = Class.forName("com.chat.util.SmileUtils");
                            Field field = clz.getField(filename);
                            mEditTextContent.append(SmileUtils.getSmiledText(ChatActivity.this, (String) field.get(null)));
                        } else { // 删除文字或者表情
                            if (!TextUtils.isEmpty(mEditTextContent.getText())) {

                                int selectionStart = mEditTextContent.getSelectionStart();// 获取光标的位置
                                if (selectionStart > 0) {
                                    String body = mEditTextContent.getText().toString();
                                    String tempStr = body.substring(0, selectionStart);
                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i, selectionStart);
                                        if (SmileUtils.containsKey(cs.toString()))
                                            mEditTextContent.getEditableText().delete(i, selectionStart);
                                        else
                                            mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                                    } else {
                                        mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                                    }
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                }

            }
        });
        return view;
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> strList = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;
            strList.add(filename);
        }
        return strList;
    }

    private PowerManager.WakeLock wakeLock;

    /**
     * 按住说话listener
     */
    class PressToSpeakListen implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!CommonUtils.isExitsSdcard()) {
                        Toast.makeText(ChatActivity.this, "发送语音需要sdcard支持！", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        wakeLock.acquire();
                        if (VoicePlayClickListener.isPlaying)
                            VoicePlayClickListener.currentPlayListener.stopPlayVoice();
                        recordingContainer.setVisibility(View.VISIBLE);
                        recordingHint.setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                        voiceRecorder.startRecording(null, toChatUsername, getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.setPressed(false);
                        if (wakeLock.isHeld())
                            wakeLock.release();
                        if (voiceRecorder != null)
                            voiceRecorder.discardRecording();
                        recordingContainer.setVisibility(View.INVISIBLE);
                        Toast.makeText(ChatActivity.this, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        recordingHint.setText(getString(R.string.release_to_cancel));
                        recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
                    } else {
                        recordingHint.setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (wakeLock.isHeld())
                        wakeLock.release();
                    if (event.getY() < 0) {
                        // discard the recorded audio.
                        voiceRecorder.discardRecording();

                    } else {
                        // stop recording and send voice file
                        try {
                            int length = voiceRecorder.stopRecoding();
                            if (length > 0) {
                                sendVoice(voiceRecorder.getVoiceFilePath(), voiceRecorder.getVoiceFileName(toChatUsername),
                                        Integer.toString(length), false);
                            } else if (length == EMError.INVALID_FILE) {
                                Toast.makeText(getApplicationContext(), "无录音权限", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "录音时间太短", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ChatActivity.this, "发送失败，请检测服务器是否连接", Toast.LENGTH_SHORT).show();
                        }

                    }
                    return true;
                default:
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (voiceRecorder != null)
                        voiceRecorder.discardRecording();
                    return false;
            }
        }
    }

    /**
     * 发送语音
     *
     * @param filePath
     * @param fileName
     * @param length
     * @param isResend
     */
    private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
        if (!(new File(filePath).exists())) {
            return;
        }
        try {
            final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP)
                message.setChatType(EMMessage.ChatType.GroupChat);
            message.setReceipt(toChatUsername);
            int len = Integer.parseInt(length);
            VoiceMessageBody body = new VoiceMessageBody(new File(filePath), len);
            message.addBody(body);
            conversation.addMessage(message);
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            setResult(RESULT_OK);
            // send file
            // sendVoiceSub(filePath, fileName, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
