package com.chat.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chat.core.Constants;
import com.chat.mobile.R;
import com.chat.util.SmileUtils;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.DateUtils;
import com.rockerhieu.emojicon.EmojiconTextView;

import java.util.Date;

public class SingleChatMessageAdapter extends BaseAdapter {
    private final static String TAG = "msg";

    private static final int MESSAGE_TYPE_RECV_TXT = 0;
    private static final int MESSAGE_TYPE_SENT_TXT = 1;
    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
    private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
    private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
    private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
    private static final int MESSAGE_TYPE_SENT_VOICE = 6;
    private static final int MESSAGE_TYPE_RECV_VOICE = 7;
    private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
    private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
    private static final int MESSAGE_TYPE_SENT_FILE = 10;
    private static final int MESSAGE_TYPE_RECV_FILE = 11;
    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 12;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 13;

    public static final String IMAGE_DIR = "chat/image/";
    public static final String VOICE_DIR = "chat/audio/";
    public static final String VIDEO_DIR = "chat/video";
    private Context context;
    private String username;
    private LayoutInflater inflater;
    private Activity activity;
    private EMConversation conversation;

    public SingleChatMessageAdapter(Context context, String username) {
        this.context = context;
        this.username = username;
        inflater = LayoutInflater.from(context);
        activity = (Activity) context;
        this.conversation = EMChatManager.getInstance().getConversation(username);
    }

    @Override
    public int getCount() {
        return conversation.getMsgCount();
    }

    @Override
    public EMMessage getItem(int position) {
        return conversation.getMessage(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private ViewHolder initImageHolder(View convertView, ViewHolder holder) {
        try {
            holder.iv = ((ImageView) convertView.findViewById(R.id.iv_sendPicture));
            holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
            holder.tv = (EmojiconTextView) convertView.findViewById(R.id.percentage);
            holder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
            holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
            holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
        } catch (Exception e) {
        }
        return holder;
    }

    private ViewHolder initTxtHolder(View convertView, ViewHolder holder, EMMessage message) {
        try {
            holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
            holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
            holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
            // 这里是文字内容
            holder.tv = (EmojiconTextView) convertView.findViewById(R.id.tv_chatcontent);
            holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
        } catch (Exception e) {
        }
        // 语音通话
        if (message.getBooleanAttribute(Constants.Easemob.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_call_icon);
            holder.tv = (EmojiconTextView) convertView.findViewById(R.id.tv_chatcontent);
        }
        return holder;
    }

    private ViewHolder initVoiceHolder(View convertView, ViewHolder holder) {
        try {
            holder.iv = ((ImageView) convertView.findViewById(R.id.iv_voice));
            holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
            holder.tv = (EmojiconTextView) convertView.findViewById(R.id.tv_length);
            holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
            holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
            holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
            holder.iv_read_status = (ImageView) convertView.findViewById(R.id.iv_unread_voice);
        } catch (Exception e) {
        }
        return holder;
    }

    private void initLocationHolder(View convertView, ViewHolder holder) {
        try {
            holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
            holder.tv = (EmojiconTextView) convertView.findViewById(R.id.tv_location);
            holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
            holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
            holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
        } catch (Exception e) {
        }
    }

    private void initVideoHolder(View convertView, ViewHolder holder) {
        try {
            holder.iv = ((ImageView) convertView.findViewById(R.id.chatting_content_iv));
            holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
            holder.tv = (EmojiconTextView) convertView.findViewById(R.id.percentage);
            holder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
            holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
            holder.size = (TextView) convertView.findViewById(R.id.chatting_size_iv);
            holder.timeLength = (TextView) convertView.findViewById(R.id.chatting_length_iv);
            holder.playBtn = (ImageView) convertView.findViewById(R.id.chatting_status_btn);
            holder.container_status_btn = (LinearLayout) convertView.findViewById(R.id.container_status_btn);
            holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);

        } catch (Exception e) {
        }
    }

    private void initFileHolder(View convertView, ViewHolder holder) {
        try {
            holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
            holder.tv_file_name = (TextView) convertView.findViewById(R.id.tv_file_name);
            holder.tv_file_size = (TextView) convertView.findViewById(R.id.tv_file_size);
            holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
            holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
            holder.tv_file_download_state = (TextView) convertView.findViewById(R.id.tv_file_state);
            holder.ll_container = (LinearLayout) convertView.findViewById(R.id.ll_file_container);
            // 这里是进度值
            holder.tv = (EmojiconTextView) convertView.findViewById(R.id.percentage);
        } catch (Exception e) {
        }
        try {
            holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
        } catch (Exception e) {
        }
    }

    private ViewHolder initHolder(View convertView, EMMessage message) {
        ViewHolder holder = new ViewHolder();
        if (message.getType() == EMMessage.Type.IMAGE) {
            initImageHolder(convertView, holder);
        } else if (message.getType() == EMMessage.Type.TXT) {
            initTxtHolder(convertView, holder, message);

        } else if (message.getType() == EMMessage.Type.VOICE) {
            initVoiceHolder(convertView, holder);
        } else if (message.getType() == EMMessage.Type.LOCATION) {
            initLocationHolder(convertView, holder);
        } else if (message.getType() == EMMessage.Type.VIDEO) {
            initVideoHolder(convertView, holder);
        } else if (message.getType() == EMMessage.Type.FILE) {
            initFileHolder(convertView, holder);
        }
        convertView.setTag(holder);
        return holder;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final EMMessage message = getItem(position);
        EMMessage.ChatType chatType = message.getChatType();
        final ViewHolder holder;
        if (convertView == null) {
            convertView = createViewByMessage(message, position);
            holder = initHolder(convertView, message);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setMessageReadStatus(message, holder, convertView);
        sendMessage(message, holder, position, convertView);
        if (message.direct == EMMessage.Direct.SEND) {
            View statusView = convertView.findViewById(R.id.msg_status);
            // 重发按钮点击事件
            statusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 显示重发消息的自定义alertdialog
                    /*Intent intent = new Intent(activity, AlertDialog.class);
                    intent.putExtra("msg", activity.getString(R.string.confirm_resend));
                    intent.putExtra("title", activity.getString(R.string.resend));
                    intent.putExtra("cancel", true);
                    intent.putExtra("position", position);
                    if (message.getType() == EMMessage.Type.TXT)
                        activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_TEXT);
                    else if (message.getType() == EMMessage.Type.VOICE)
                        activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_VOICE);
                    else if (message.getType() == EMMessage.Type.IMAGE)
                        activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_PICTURE);
                    else if (message.getType() == EMMessage.Type.LOCATION)
                        activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_LOCATION);
                    else if (message.getType() == EMMessage.Type.FILE)
                        activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_FILE);
                    else if (message.getType() == EMMessage.Type.VIDEO)
                        activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_VIDEO);*/
                }
            });

        } else {
            // 长按头像，移入黑名单
            holder.head_iv.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    /*Intent intent = new Intent(activity, AlertDialog.class);
                    intent.putExtra("msg", "移入到黑名单？");
                    intent.putExtra("cancel", true);
                    intent.putExtra("position", position);
                    activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_ADD_TO_BLACKLIST);*/
                    return true;
                }
            });
        }

        TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
        if (position == 0) {
            timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
            timestamp.setVisibility(View.VISIBLE);
        } else {
            // 两条消息时间离得如果稍长，显示时间
            if (DateUtils.isCloseEnough(message.getMsgTime(), conversation.getMessage(position - 1).getMsgTime())) {
                timestamp.setVisibility(View.GONE);
            } else {
                timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                timestamp.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    private void setMessageReadStatus(EMMessage message, ViewHolder holder, View convertView) {
        // 如果是发送的消息并且不是群聊消息，显示已读textview
        if (message.direct == EMMessage.Direct.SEND) {
            holder.tv_ack = (TextView) convertView.findViewById(R.id.tv_ack);
            holder.tv_delivered = (TextView) convertView.findViewById(R.id.tv_delivered);
            if (holder.tv_ack != null) {
                if (message.isAcked) {
                    if (holder.tv_delivered != null) {
                        holder.tv_delivered.setVisibility(View.INVISIBLE);
                    }
                    holder.tv_ack.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_ack.setVisibility(View.INVISIBLE);

                    // check and display msg delivered ack status
                    if (holder.tv_delivered != null) {
                        if (message.isDelivered) {
                            holder.tv_delivered.setVisibility(View.VISIBLE);
                        } else {
                            holder.tv_delivered.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        } else {
            // 如果是文本或者地图消息并且不是group messgae，显示的时候给对方发送已读回执
            if ((message.getType() == EMMessage.Type.TXT || message.getType() == EMMessage.Type.LOCATION) && !message.isAcked) {
                // 不是语音通话记录
                if (!message.getBooleanAttribute(Constants.Easemob.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    try {
                        EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
                        // 发送已读回执
                        message.isAcked = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void sendMessage(EMMessage message, ViewHolder holder, int position, View convertView) {
        switch (message.getType()) {
            // 根据消息type显示item
            case IMAGE: // 图片
                handleImageMessage(message, holder, position, convertView);
                break;
            case TXT: // 文本
                if (!message.getBooleanAttribute(Constants.Easemob.MESSAGE_ATTR_IS_VOICE_CALL, false))
                    handleTextMessage(message, holder, position);
                else
                    // 语音电话
                    handleVoiceCallMessage(message, holder, position);
                break;
            case LOCATION: // 位置
                handleLocationMessage(message, holder, position, convertView);
                break;
            case VOICE: // 语音
                handleVoiceMessage(message, holder, position, convertView);
                break;
            case VIDEO: // 视频
                handleVideoMessage(message, holder, position, convertView);
                break;
            case FILE: // 一般文件
                handleFileMessage(message, holder, position, convertView);
                break;
            default:
                // not supported
        }
    }

    /**
     * 获取item类型
     */
    @Override
    public int getItemViewType(int position) {
        EMMessage message = conversation.getMessage(position);
        if (message.getType() == EMMessage.Type.TXT) {
            if (!message.getBooleanAttribute(Constants.Easemob.MESSAGE_ATTR_IS_VOICE_CALL, false))
                return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
        }
        if (message.getType() == EMMessage.Type.IMAGE) {
            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;

        }
        if (message.getType() == EMMessage.Type.LOCATION) {
            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
        }
        if (message.getType() == EMMessage.Type.VOICE) {
            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
        }
        if (message.getType() == EMMessage.Type.VIDEO) {
            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
        }
        if (message.getType() == EMMessage.Type.FILE) {
            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
        }
        return -1;// invalid
    }

    @Override
    public int getViewTypeCount() {
        return 14;
    }

    private View createViewByMessage(EMMessage message, int position) {
        switch (message.getType()) {
            case LOCATION:
                return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_location, null) : inflater.inflate(
                        R.layout.row_sent_location, null);
            case IMAGE:
                return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_picture, null) : inflater.inflate(
                        R.layout.row_sent_picture, null);

            case VOICE:
                return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_voice, null) : inflater.inflate(
                        R.layout.row_sent_voice, null);
            case VIDEO:
                return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_video, null) : inflater.inflate(
                        R.layout.row_sent_video, null);
            case FILE:
                return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_file, null) : inflater.inflate(
                        R.layout.row_sent_file, null);
            default:
                // 语音电话
                if (message.getBooleanAttribute(Constants.Easemob.MESSAGE_ATTR_IS_VOICE_CALL, false))
                    return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_voice_call, null) : inflater
                            .inflate(R.layout.row_sent_voice_call, null);
                return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_message, null) : inflater.inflate(
                        R.layout.row_sent_message, null);
        }
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        ImageView iv;
        EmojiconTextView tv;
        ProgressBar pb;
        ImageView staus_iv;
        ImageView head_iv;
        TextView tv_userId;
        ImageView playBtn;
        TextView timeLength;
        TextView size;
        LinearLayout container_status_btn;
        LinearLayout ll_container;
        ImageView iv_read_status;
        // 显示已读回执状态
        TextView tv_ack;
        // 显示送达回执状态
        TextView tv_delivered;

        TextView tv_file_name;
        TextView tv_file_size;
        TextView tv_file_download_state;
    }

    /**
     * 文本消息
     *
     * @param message
     * @param holder
     * @param position
     */
    private void handleTextMessage(EMMessage message, ViewHolder holder, final int position) {
        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        // 设置内容
        holder.tv.setText(txtBody.getMessage());
        // 设置长按事件监听
        holder.tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               /* activity.startActivityForResult(
                        (new Intent(activity, ContextMenu.class)).putExtra("position", position).putExtra("type",
                                EMMessage.Type.TXT.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);*/
                return true;
            }
        });

        if (message.direct == EMMessage.Direct.SEND) {
            switch (message.status) {
                case SUCCESS: // 发送成功
                    holder.pb.setVisibility(View.GONE);
                    holder.staus_iv.setVisibility(View.GONE);
                    break;
                case FAIL: // 发送失败
                    holder.pb.setVisibility(View.GONE);
                    holder.staus_iv.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS: // 发送中
                    holder.pb.setVisibility(View.VISIBLE);
                    holder.staus_iv.setVisibility(View.GONE);
                    break;
                default:
                    // 发送消息
                    sendMsgInBackground(message, holder);
            }
        }
    }

    /**
     * 语音通话记录
     *
     * @param message
     * @param holder
     * @param position
     */
    private void handleVoiceCallMessage(EMMessage message, ViewHolder holder, final int position) {
        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        holder.tv.setText(txtBody.getMessage());

    }

    /**
     * 图片消息
     *
     * @param message
     * @param holder
     * @param position
     * @param convertView
     */
    private void handleImageMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {

    }

    /**
     * 视频消息
     *
     * @param message
     * @param holder
     * @param position
     * @param convertView
     */
    private void handleVideoMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {

    }

    /**
     * 语音消息
     *
     * @param message
     * @param holder
     * @param position
     * @param convertView
     */
    private void handleVoiceMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {

    }

    /**
     * 文件消息
     *
     * @param message
     * @param holder
     * @param position
     * @param convertView
     */
    private void handleFileMessage(final EMMessage message, final ViewHolder holder, int position, View convertView) {

    }

    /**
     * 处理位置消息
     *
     * @param message
     * @param holder
     * @param position
     * @param convertView
     */
    private void handleLocationMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {

    }

    /**
     * 发送消息
     *
     * @param message
     * @param holder
     */
    public void sendMsgInBackground(final EMMessage message, final ViewHolder holder) {

    }

    /*
     * chat sdk will automatic download thumbnail image for the image message we
     * need to register callback show the download progress
     */
    private void showDownloadImageProgress(final EMMessage message, final ViewHolder holder) {

    }

    /*
     * send message with new sdk
     */
    private void sendPictureMessage(final EMMessage message, final ViewHolder holder) {

    }

    /**
     * 更新ui上消息发送状态
     *
     * @param message
     * @param holder
     */
    private void updateSendedView(final EMMessage message, final ViewHolder holder) {

    }

    /**
     * load image into image view
     *
     * @param thumbernailPath
     * @param iv
     * @param
     * @return the image exists or not
     */
    private boolean showImageView(final String thumbernailPath, final ImageView iv, final String localFullSizePath, String remoteDir,
                                  final EMMessage message) {
        return true;
    }

    /**
     * 展示视频缩略图
     *
     * @param localThumb   本地缩略图路径
     * @param iv
     * @param thumbnailUrl 远程缩略图路径
     * @param message
     */
    private void showVideoThumbView(String localThumb, ImageView iv, String thumbnailUrl, final EMMessage message) {


    }

}
