package com.example.ganshenml.tomatoman.tool;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.ganshenml.tomatoman.R;

/**
 * Created by ganshenml on 2016/4/19.
 * 发送通知的工具类
 */
public class NotificationUtls {

    public static void sendNotification(Context activity,int REQUEST_CODE,Intent intent,int layoutId){
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, REQUEST_CODE, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity);
        builder.setAutoCancel(false).setTicker("开始工作啦~~~")
                .setUsesChronometer(true)
                .setSmallIcon(R.drawable.tomato_icon)//小图标，也是手机栏顶上的显示图标
                .setContentIntent(pendingIntent)//点击后的事件
                .setContent(getRemoteViews(activity,layoutId))//自定义通知的view
                .setOngoing(true);//设置不能手动删除
        notificationManager.notify(REQUEST_CODE, builder.build());//使用常量作为该通知的唯一标识

    }

    //根据传过来的REQUEST_CODE（notification的唯一标识）来取消该notification的显示
    public static void cancelNotification(Context activity,int REQUEST_CODE){
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(REQUEST_CODE);
    }

   static RemoteViews getRemoteViews(Context activity , int layoutId) {
        RemoteViews remoteViews = new RemoteViews(activity.getPackageName(),
                layoutId);

       //根据传过来的不同layoutId来加载不同的显示样式
       switch (layoutId){
           case R.layout.notification_layout:
               remoteViews.setTextViewText(R.id.tvNotiTaskName, "这是工作中的内容");//设置通知中的内容
               remoteViews.setChronometer(R.id.chronometerCount, SystemClock.elapsedRealtime
                       (), null, true);//设置通知中的默认显示时间
               break;
           case R.layout.notification_rest_layout:
               remoteViews.setChronometer(R.id.chronometerCount_rest, SystemClock.elapsedRealtime
                       (), null, true);//设置通知中的默认显示时间
               break;
           case R.layout.notification_efficiency_layout:
               remoteViews.setTextViewText(R.id.tvNotiTaskName_efficiency, "这是高效工作中的内容");//设置通知中的内容
               remoteViews.setChronometer(R.id.chronometerCount_efficiency, SystemClock.elapsedRealtime
                       (), null, true);//设置通知中的默认显示时间
               break;
           default:break;
       }
        return remoteViews;
    }
}
