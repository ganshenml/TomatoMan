package com.example.ganshenml.tomatoman.util;

/**
 * Created by ganshenml on 2016-07-20.
 */

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
/**
 * Created by cyan on 15/9/21.
 * 线程工具类
 */
public final class ThreadTool {

    private static BgThread bgThread;

    static {
        bgThread =new BgThread();
        bgThread.start();
    }

    public static void runOnBgThread(final Runnable action){
        Handler handler =new Handler(bgThread.looper){
            @Override
            public void handleMessage(Message msg) {
                action.run();
            }
        };
        handler.sendEmptyMessage(0);
    }

    public static void runOnUiThread(Runnable action) {
        if(Thread.currentThread().getId()!=0) {
            Handler handler = new UiHandler(action);
            handler.sendEmptyMessage(0);
        }else{
            action.run();
        }
    }

    public static void runOnUiThread(final Runnable action, final long dellay) {
        runOnNewThread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(dellay);
                runOnUiThread(action);
            }
        });
    }

    public static void runOnNewThread(Runnable action) {
        new Thread(action).start();
    }

    public static void runOnNewThread(final Runnable action, final long delay) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(delay);
                action.run();
            }
        }).start();
    }

    public static void showCurThreadInfo(){
        Thread cur= Thread.currentThread();
        Log.d("thread", "当前线程,serverId:" + cur.getId() + " name:" + cur.getName());
    }

    static class BgThread extends Thread {

        BgThread(){
            super("cyan-bg-thread");
        }
        Looper looper;
        @Override
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            Looper.loop();
        }
    }


    /**
     * 自定义handler，用以切换到主线程
     *
     * @author hdr
     */
    static class UiHandler extends Handler {
        Runnable action;

        UiHandler(Runnable action) {
            super(Looper.getMainLooper());
            this.action = action;
        }

        @Override
        public void handleMessage(Message msg) {
            action.run();
        }
    }
}
