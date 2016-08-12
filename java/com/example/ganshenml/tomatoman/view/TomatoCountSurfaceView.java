package com.example.ganshenml.tomatoman.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by ganshenml on 2016/4/12.
 */
public class TomatoCountSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder surfaceHolder;
    public CountThread countThread;
    private float endAngle = 0;//计算一次要画的角度大小
    private float divisionNum ;//计算每秒走多少度
    private boolean isThreadStarted = false;//作为线程是否启动的标识
//    private boolean isFirstShown = false;//作为一开始计时时快速走过一圈的动画（仅一次展示）


    public TomatoCountSurfaceView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        countThread = new CountThread(surfaceHolder);
    }

    public TomatoCountSurfaceView(Context context, AttributeSet attributeSet) {
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
        SurfaceHolder surfaceHolder;
        public boolean isStop = false;
        Paint paint, paintArcBackgroud, paintText;

        //线程构造方法中做一些初始化的工作
        public CountThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
            isStop = false;

            //圆弧的画笔初始化
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(20);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLUE);

            //背景圆弧的画笔初始化
            paintArcBackgroud = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintArcBackgroud.setStrokeWidth(19);
            paintArcBackgroud.setStyle(Paint.Style.STROKE);
            paintArcBackgroud.setColor(Color.parseColor("#C6E2FF"));

            //计时数字的画笔初始化
            paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setTextSize(128);
            paintText.setColor(Color.parseColor("#C6E2FF"));
            paintText.setTextAlign(Paint.Align.CENTER);
        }

        @Override
        public void run() {
            Canvas canvas = null;
            int pivotX = getResources().getDisplayMetrics().widthPixels / 2;
            RectF rectF = new RectF(pivotX - 300, pivotX - 300, pivotX + 300, pivotX + 300);
            int count = 0;//计时（当前跑了多久）

            while (!isStop) {
                try {
                    canvas = surfaceHolder.lockCanvas();
                    canvas.drawColor(Color.WHITE);//设置画布背景为白色
//                    canvas.drawRoundRect(300, 300, 600, 600, 150, 150, paint);//直接使用该行代码来画圆是行不通的，因为这个方法要求版本21，我的手机运行android版本是19
                    //画一个背景圆弧
                    canvas.drawArc(rectF, -90, 360, false, paintArcBackgroud);

                    endAngle = endAngle + 1;
                    canvas.drawArc(rectF, -90, endAngle*divisionNum, false, paint);//-90在这里不等于270，所以要想从最上方开始画弧，就得用 - 90
                    canvas.drawText(countTime((int)endAngle), pivotX, pivotX, paintText);//显示计算的时间
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
