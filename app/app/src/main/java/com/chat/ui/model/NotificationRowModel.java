package com.chat.ui.model;


import com.easemob.chat.EMGroup;
import com.easemob.chat.EMMessage;

public class NotificationRowModel {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    private boolean isGroup;

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }
    private int unreadMsgCount;

    public int getUnreadMsgCount() {
        return unreadMsgCount;
    }

    public void setUnreadMsgCount(int unreadMsgCount) {
        this.unreadMsgCount = unreadMsgCount;
    }
    private int msgCount;

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }
    private EMMessage lastMessage;

    public EMMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(EMMessage lastMessage) {
        this.lastMessage = lastMessage;
    }
    private EMGroup emGroup;

    public EMGroup getEmGroup() {
        return emGroup;
    }

    public void setEmGroup(EMGroup emGroup) {
        this.emGroup = emGroup;
    }
}
