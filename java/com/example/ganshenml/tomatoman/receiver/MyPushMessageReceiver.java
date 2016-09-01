package com.example.ganshenml.tomatoman.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.ganshenml.tomatoman.tool.LogTool;

import cn.bmob.push.PushConstants;

/**
 * Created by ganshenml on 2016-09-01.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            LogTool.log(LogTool.Aaron, "MyPushMessageReceiver 客户端收到推送内容："+intent.getStringExtra("msg"));
        }
    }

}