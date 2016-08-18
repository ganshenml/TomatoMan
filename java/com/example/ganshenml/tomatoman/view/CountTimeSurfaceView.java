package com.example.ganshenml.tomatoman.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 计算的文字样式
 * Created by ganshenml on 2016-08-17.
 */

public class CountTimeSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    public CountThread countThread;
    private float endAngle = 0;//计算一次要画的角度大小
    private float divisionNum;//计算每秒走多少度
    private boolean isThreadStarted = false;//作为线程是否启动的标识


    public CountTimeSurfaceView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        countThread = new CountThread(surfaceHolder);
    }

    public CountTimeSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        countThread = new CountThread(surfaceHolder);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (isThreadStarted == false) {//如果线程未被启动，则启动线程——>当应用挂起的时候Thread是存在的，如果不做这个判断，会报“Thread already started”错误
            countThread.start();//SurfaceView创建时开启线程
            isThreadStarted = true;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public class CountThread extends Thread {
        private SurfaceHolder surfaceHolder;
        public boolean isStop = false;
        private Paint paintText;

        //线程构造方法中做一些初始化的工作
        public CountThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
            isStop = false;

            //计时数字的画笔初始化
            paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setTextSize(128);
            paintText.setColor(Color.parseColor("#FF6600"));
            paintText.setTextAlign(Paint.Align.CENTER);
            paintText.setShadowLayer(1, 3, 3, Color.parseColor("#FFFFFF"));

        }

        @Override
        public void run() {

            Canvas canvas = null;
            int pivotX = getResources().getDisplayMetrics().widthPixels / 2;
            RectF rectFB = new RectF(pivotX - 325, pivotX - 325, pivotX + 325, pivotX + 325);
            RectF rectF = new RectF(pivotX - 300, pivotX - 300, pivotX + 300, pivotX + 300);
            int count = 0;//计时（当前跑了多久）

            while (!isStop) {
                try {
                    canvas = surfaceHolder.lockCanvas();
//                    canvas.drawColor(Color.WHITE);//设置画布背景为白色

                    endAngle = endAngle + 1;
                    canvas.drawText(countTime((int) endAngle), pivotX, pivotX, paintText);//显示计算的时间

                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {//需要对canvas进行非空判断
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        //根据秒数计算时间格式
        private String countTime(int endAngle) {
            String timeStr = "";
            int minuteInt = endAngle / 60;//分钟数值
            int secondInt = endAngle % 60;//秒数数值
            if (minuteInt > 10) {
                if (secondInt < 10) {
                    timeStr = String.valueOf(minuteInt) + ":0" + String.valueOf(secondInt);
                } else {
                    timeStr = String.valueOf(minuteInt) + ":" + String.valueOf(secondInt);
                }
            } else if (minuteInt >= 0 && minuteInt < 10) {
                if (secondInt < 10) {
                    timeStr = "0" + String.valueOf(minuteInt) + ":0" + String.valueOf
                            (secondInt);
                } else {
                    timeStr = "0" + String.valueOf(minuteInt) + ":" + String.valueOf
                            (secondInt);
                }
            }
            return timeStr;
        }
    }

    public void setEndAngle(float endAngle) {
        this.endAngle = endAngle;
    }

    public void setDivisionNum(float divisionNum) {
        this.divisionNum = divisionNum;
    }


}
