package com.chat.ui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chat.core.Constants;
import com.chat.mobile.R;
import com.chat.ui.model.NotificationRowModel;
import com.chat.util.SmileUtils;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.DateUtils;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationListAdapter extends ArrayAdapter<NotificationRowModel> {
    private LayoutInflater inflater;
    private List<NotificationRowModel> copyConversationList;

    public NotificationListAdapter(Context context, int textViewResourceId, List<NotificationRowModel> messageRowModels) {
        super(context, textViewResourceId, messageRowModels);
        copyConversationList = new ArrayList<NotificationRowModel>();
        copyConversationList.addAll(messageRowModels);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_notification, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.message = (EmojiconTextView) convertView.findViewById(R.id.message);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.msgState = convertView.findViewById(R.id.msg_state);
            holder.list_item_layout = (RelativeLayout) convertView.findViewById(R.id.list_item_layout);
            convertView.setTag(holder);
        }
        if (position % 2 == 0) {
            holder.list_item_layout.setBackgroundResource(R.drawable.mm_listitem);
        } else {
            holder.list_item_layout.setBackgroundResource(R.drawable.mm_listitem_grey);
        }

        // 获取与此用户/群组的会话
        NotificationRowModel rowModel = getItem(position);
        // 获取用户username或者群组groupid
        String username = rowModel.getName();
        EMContact contact;
        boolean isGroup = rowModel.isGroup();
        if (isGroup) {
            contact = rowModel.getEmGroup();
            // 群聊消息，显示群聊头像
            holder.avatar.setImageResource(R.drawable.group_icon);
            holder.name.setText(contact.getNick() != null ? contact.getNick() : username);
        } else {
            // 本地或者服务器获取用户详情，以用来显示头像和nick
            Picasso p = Picasso.with(getContext());

            p.setIndicatorsEnabled(true);
            p.load("http://f.hiphotos.baidu.com/image/pic/item/cdbf6c81800a19d8a1d53c0c31fa828ba71e46e0.jpg")
                    .error(R.drawable.default_avatar).resize(60, 60).into(holder.avatar);
            //holder.avatar.setImageResource(R.drawable.default_avatar);
            if (username.equals(Constants.Easemob.GROUP_USERNAME)) {
                holder.name.setText("群聊");

            } else if (username.equals(Constants.Easemob.NEW_FRIENDS_USERNAME)) {
                holder.name.setText("申请与通知");
            }
            holder.name.setText(username);
        }

        if (rowModel.getUnreadMsgCount() > 0) {
            // 显示与此用户的消息未读数
            holder.unreadLabel.setText(String.valueOf(rowModel.getUnreadMsgCount()));
            holder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
        }

        if (rowModel.getMsgCount() != 0) {
            // 把最后一条消息的内容作为item的message内容
            EMMessage lastMessage = rowModel.getLastMessage();
            if (lastMessage != null) {
                holder.message.setText(getMessageDigest(lastMessage, (this.getContext())),
                        TextView.BufferType.SPANNABLE);
                holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
                if (lastMessage.direct == EMMessage.Direct.SEND && lastMessage.status == EMMessage.Status.FAIL) {
                    holder.msgState.setVisibility(View.VISIBLE);
                } else {
                    holder.msgState.setVisibility(View.GONE);
                }
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        /**
         * 和谁的聊天记录
         */
        TextView name;
        /**
         * 消息未读数
         */
        TextView unreadLabel;
        /**
         * 最后一条消息的内容
         */
        EmojiconTextView message;
        /**
         * 最后一条消息的时间
         */
        TextView time;
        /**
         * 用户头像
         */
        ImageView avatar;
        /**
         * 最后一条消息的发送状态
         */
        View msgState;
        /**
         * 整个list中每一行总布局
         */
        RelativeLayout list_item_layout;

    }

    /**
     * 根据消息内容和消息类型获取消息内容提示
     *
     * @param message 消息
     * @param context context
     * @return 返回消息提示内容
     */
    private String getMessageDigest(EMMessage message, Context context) {
        String digest;
        switch (message.getType()) {
            case LOCATION: // 位置消息
                if (message.direct == EMMessage.Direct.RECEIVE) {
                    // 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_recv");
                    digest = getStrng(context, R.string.location_recv);
                    digest = String.format(digest, message.getFrom());
                    return digest;
                } else {
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_prefix");
                    digest = getStrng(context, R.string.location_prefix);
                }
                break;
            case IMAGE: // 图片消息
                ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
                digest = getStrng(context, R.string.picture) + imageBody.getFileName();
                break;
            case VOICE:// 语音消息
                digest = getStrng(context, R.string.voice);
                break;
            case VIDEO: // 视频消息
                digest = getStrng(context, R.string.video);
                break;
            case TXT: // 文本消息
                if (!message.getBooleanAttribute(Constants.Easemob.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = txtBody.getMessage();
                } else {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = getStrng(context, R.string.voice_call) + txtBody.getMessage();
                }
                break;
            case FILE: // 普通文件消息
                digest = getStrng(context, R.string.file);
                break;
            default:
                System.err.println("error, unknow type");
                return "";
        }

        return digest;
    }

    String getStrng(Context context, int resId) {
        return context.getResources().getString(resId);
    }
}
