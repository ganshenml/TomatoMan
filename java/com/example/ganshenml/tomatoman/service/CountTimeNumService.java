package com.example.ganshenml.tomatoman.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ganshenml.tomatoman.receiver.TimeArriveReceiver;
import com.example.ganshenml.tomatoman.tool.LogTool;

/**
 * Created by ganshenml on 2016/4/13.
 * 后台Service在后台继续计算传过来的时间值，到点时间则发送broadcastReceiver（在receiver中进行activity的finish和跳转）
 */
public class CountTimeNumService extends Service {

    private float countTimeNum = 0;
    private CountTimeNumThread countTimeNumThread;


    private IBinder myBinder = new MyBinder();
    private boolean stopFlag = false;
    private Context activity;//接收这个activity
    private int countTimeGoal = 0;//计时的目标时间，由activity传过来

    public class MyBinder extends Binder {
        public CountTimeNumService getService() {
            return CountTimeNumService.this;
        }

        public float getCountedTime (){
            return countTimeNum;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        countTimeGoal = intent.getIntExtra("countTimeGoal", 0) * 60;//以秒为单位
        countTimeNumThread = new CountTimeNumThread();
        LogTool.log(LogTool.Aaron, "计时的服务线程启动了");
        countTimeNumThread.start();
        return myBinder;
    }


    //新开线程开始计算时间
    class CountTimeNumThread extends Thread {
        @Override
        public void run() {
            super.run();
            if (countTimeGoal == 0) {
                Log.e("countTimeGoal", "就是0");
                return;
            }
//            float divisionNum = 360 / countTimeGoal;//计算每秒走多少度

            while (!stopFlag) {
                try {
                    LogTool.log(LogTool.Aaron, "countTimeNum的值和countTimeGoal的值： " + countTimeNum + "   " + countTimeGoal);
                    if (countTimeNum < countTimeGoal) {//未到目标时间继续计时
                        countTimeNum = countTimeNum + 1;
                        Thread.sleep(1000);
                    } else {//到达目标计时时间则停止计时，并发送计时完成的广播给broadcastReceiver
                        stopFlag = true;
                        Intent intent = new Intent(CountTimeNumService.this, TimeArriveReceiver.class);
                        LogTool.log(LogTool.Aaron, "计时完成了");
                        intent.putExtra("completeTime", countTimeNum);
                        sendBroadcast(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countTimeNumThread != null) {
            if (countTimeNumThread.isAlive()) {
                countTimeNumThread.interrupt();//销毁线程
            }
        }
    }

    public float getCountTimeNum() {
        return countTimeNum;
    }

    public void setStopFlag(boolean stopFlag) {
        this.stopFlag = stopFlag;
    }

    public void setActivity(Context activity) {
        this.activity = activity;
    }

//    public void setCountTimeGoal(int countTimeGoal) {
//        this.countTimeGoal = countTimeGoal;
//        LogTool.log(LogTool.Aaron,"countTimeGoalService 要计时的时间是： "+countTimeGoal);
//    }


    public void setCountTimeNumThread(CountTimeNumThread countTimeNumThread) {
        this.countTimeNumThread = countTimeNumThread;
    }
}
