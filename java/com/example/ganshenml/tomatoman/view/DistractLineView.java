package com.example.ganshenml.tomatoman.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.ganshenml.tomatoman.R;

/**
 * Created by ganshenml on 2016-08-17.
 */
public class DistractLineView extends View {
    private Paint paintCircle, paintArc, paintLine, paintDottedLine, paintBitmap;
    private int stageNum = 0;

    public DistractLineView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(Color.parseColor("#FF6600"));

        paintArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintArc.setStrokeWidth(10);
        paintArc.setColor(Color.parseColor("#FFFFFF"));

        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLine.setStrokeWidth(4);
        paintLine.setColor(Color.parseColor("#FF6600"));

        //虚线
        paintDottedLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintDottedLine.setStyle(Paint.Style.STROKE);
        paintDottedLine.setStrokeWidth(4);
        paintDottedLine.setColor(Color.parseColor("#999999"));
        PathEffect effects = new DashPathEffect(new float[] { 4, 8}, 0);
        paintDottedLine.setPathEffect(effects);

        paintBitmap = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int[] pivotX = new int[]{(int) (width / 4.0), (int) (width / 2.0), (int) (width * 5 / 8.0), (int) (width * 3 / 4.0), (int) (width * 7 / 8.0)};
        int[] pivotY = new int[]{(int) (height * 4 / 7.0), (int) (height / 2.0), (int) (height * 3 / 8.0), (int) (height / 4.0), (int) (height / 8.0)};

        for (int i = 0; i < stageNum; i++) {
            RectF rectFArc = new RectF(pivotX[i] - 30, pivotY[i] - 30, pivotX[i] + 30, pivotY[i] + 30);
            canvas.drawArc(rectFArc, -90, 360, false, paintArc);

            RectF rectF = new RectF(pivotX[i] - 20, pivotY[i] - 20, pivotX[i] + 20, pivotY[i] + 20);
            canvas.drawRoundRect(rectF, pivotX[i], pivotY[i], paintCircle);

            if (i < stageNum - 1) {
                float[] pts = new float[]{pivotX[i] + 22, pivotY[i] - 18, pivotX[i + 1] - 18, pivotY[i + 1] + 18};
                canvas.drawLines(pts, paintLine);
            } else if (i == stageNum - 1 && stageNum != 5) {//此时画下一个点和虚线
                RectF rectFArc2 = new RectF(pivotX[i + 1] - 30, pivotY[i + 1] - 30, pivotX[i + 1] + 30, pivotY[i + 1] + 30);
                canvas.drawArc(rectFArc2, -90, 360, false, paintArc);

                RectF rectF2 = new RectF(pivotX[i + 1] - 20, pivotY[i + 1] - 20, pivotX[i + 1] + 20, pivotY[i + 1] + 20);
                canvas.drawRoundRect(rectF2, pivotX[i + 1], pivotY[i + 1], paintCircle);

//                canvas.drawLine(pivotX[i] + 22, pivotY[i] - 18, pivotX[i + 1] - 18, pivotY[i + 1] + 18, paintDottedLine);

                Path path = new Path();
                path.moveTo(pivotX[i] + 22, pivotY[i] - 18);
                path.lineTo(pivotX[i + 1] - 18, pivotY[i + 1] + 18);
                canvas.drawPath(path, paintDottedLine);
            }
        }

        if (stageNum == 5) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.star);
            RectF rectF = new RectF(pivotX[4] - 60, pivotY[4] - 60, pivotX[4] + 60, pivotY[4] + 60);
            canvas.drawBitmap(bitmap, null, rectF, paintBitmap);
        }
    }

    public void setStageNum(int stageNum) {
        this.stageNum = stageNum;
    }
}
