package com.chat;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.chat.core.ChatService;
import com.chat.mobile.R;
import com.chat.ui.SingleChatFragment;
import com.chat.ui.model.User;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by HQ_19 on 2014/12/2.
 */
public class ChatApplication extends Application {
    private static ChatApplication instance;

    /**
     * Create main application
     */
    public ChatApplication() {
    }

    /**
     * Create main application
     *
     * @param context
     */
    public ChatApplication(final Context context) {
        this();
        attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // Perform injection
        Injector.init(getRootModule(), this);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果使用到百度地图或者类似启动remote service的第三方库，这个if判断不能少
        if (processAppName == null || processAppName.equals("")) {
            // workaround for baidu location sdk
            // 百度定位sdk，定位服务运行在一个单独的进程，每次定位服务启动的时候，都会调用application::onCreate
            // 创建新的进程。
            // 但环信的sdk只需要在主进程中初始化一次。 这个特殊处理是，如果从pid 找不到对应的processInfo
            // processName，
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //初始化环信SDK
        Log.d("DemoApplication", "Initialize EMChat SDK");
        EMChat.getInstance().init(instance);
        //获取到EMChatOptions对象
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        options.setOnNotificationClickListener(new OnNotificationClickListener() {
            @Override
            public Intent onNotificationClick(EMMessage message) {
                Intent intent = new Intent(getApplicationContext(), SingleChatFragment.class);
                EMMessage.ChatType chatType = message.getChatType();
                if (chatType == EMMessage.ChatType.Chat) { //单聊信息
                    intent.putExtra("userId", message.getFrom());
                } else { //群聊信息
                    //message.getTo()为群聊id
                    intent.putExtra("groupId", message.getTo());
                }
                return intent;
            }
        });
        options.setNotifyText(new OnMessageNotifyListener() {
            @Override
            public String onNewMessageNotify(EMMessage message) {
                return "新消息内容";
            }

            @Override
            public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
                return "你收到了" + messageNum + "条新消息";
            }

            @Override
            public String onSetNotificationTitle(EMMessage message) {
                return "老乡村";
            }

            @Override
            public int onSetSmallIcon(EMMessage message) {
                return R.drawable.ee_1;
            }
        });
        options.setNotifyBySoundAndVibrate(true); //默认为true 开启新消息提醒
        options.setNoticeBySound(true); //默认为true 开启声音提醒
        options.setNoticedByVibrate(true); //默认为true 开启震动提醒
        options.setUseSpeaker(true); //默认为true 开启扬声器播放
        options.setShowNotificationInBackgroud(true); //默认为true
        options.setAcceptInvitationAlways(false);
        //默认添加好友时为true，是不需要验证的，改成需要验证为false)
    }

    private Object getRootModule() {
        return new RootModule();
    }


    /**
     * Create main application
     *
     * @param instrumentation
     */
    public ChatApplication(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }

    public static ChatApplication getInstance() {
        return instance;
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
                    .next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm
                            .getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }

    /**
     * 获取当前登陆用户名
     *
     * @return
     */
    public String getUserName() {
        return EMChatManager.getInstance().getCurrentUser();
    }
}
