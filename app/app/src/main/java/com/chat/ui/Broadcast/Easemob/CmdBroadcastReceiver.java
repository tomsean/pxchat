package com.chat.ui.Broadcast.Easemob;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMMessage;

/**
 * Created by HQ_19 on 2014/12/17.
 */
public class CmdBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            //获取cmd message对象
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = intent.getParcelableExtra("message");
            //获取消息body
            CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
            String aciton = cmdMsgBody.action;//获取自定义action
            //获取扩展属性
            String attr=message.getStringAttribute("a");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
