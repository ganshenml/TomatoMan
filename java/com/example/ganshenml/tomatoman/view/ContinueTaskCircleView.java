package com.example.ganshenml.tomatoman.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by ganshenml on 2016/4/17.
 * 绘制圆形可点击的圆形：继续任务
 */
public class ContinueTaskCircleView extends View {
    Paint paintCircle,paintText;
    public ContinueTaskCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTools();//初始化画笔
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画外侧的圆
        canvas.drawCircle(131, 131, 130, paintCircle);

        //画文字
        canvas.drawText("继续任务",130,150,paintText);
    }

    //----------------------------------------------------------------以下为自定义方法---------------------------------------------
    private void initTools() {

        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(Color.GREEN);
        paintCircle.setStrokeWidth(1);
        paintCircle.setStyle(Paint.Style.STROKE);

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.GREEN);
        paintText.setStrokeWidth(1);
        paintText.setTextSize(48);
        paintText.setTextAlign(Paint.Align.CENTER);

    }
}
