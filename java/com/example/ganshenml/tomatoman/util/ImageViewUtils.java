package com.example.ganshenml.tomatoman.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Log;

public class ImageViewUtils {

    //得到圆形图片
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        if (bitmap == null) {
            Log.e("bitmap", "为空");
            return null;
        }
        Bitmap output = Bitmap.createBitmap(2 * pixels,
                2 * pixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final RectF rectF = new RectF(0, 0, 2 * pixels, 2 * pixels);

        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        canvas.drawRoundRect(rectF, pixels, pixels, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rectF, paint);

        return output;
    }
}