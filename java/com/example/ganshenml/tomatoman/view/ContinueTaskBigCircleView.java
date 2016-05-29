package com.example.ganshenml.tomatoman.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ganshenml on 2016/4/16.
 * 用来画继续任务的大大大圆view
 */
public class ContinueTaskBigCircleView extends View {
    Paint paintCircle,paintText;
    public ContinueTaskBigCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTools();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int pivotX = getResources().getDisplayMetrics().widthPixels / 2;
        //画外侧的圆
        canvas.drawCircle(pivotX,pivotX,300,paintCircle);
        //画圆内的文字
        canvas.drawText("继续任务",pivotX,pivotX+30,paintText);
    }


    //----------------------------------------------------------------以下为自定义方法---------------------------------------------
    private void initTools() {

        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(Color.GREEN);
        paintCircle.setStrokeWidth(1);
        paintCircle.setStyle(Paint.Style.STROKE);

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.GREEN);
        paintText.setStrokeWidth(4);
        paintText.setTextSize(64);
        paintText.setTextAlign(Paint.Align.CENTER);
    }
}
