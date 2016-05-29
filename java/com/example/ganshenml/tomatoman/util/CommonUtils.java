package com.example.ganshenml.tomatoman.util;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by ganshenml on 2016/4/23.
 * 常用的工具类：1.计时任务完成时的振动
 */

public class CommonUtils {

    //计时任务自动完成时的振动方法
    public static void startVibrate(Context context) {
        long[] patterns = new long[]{0, 1000, 2000, 1000, 2000, 1000};
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(patterns, -1);//不重复，仅一次
    }
}
