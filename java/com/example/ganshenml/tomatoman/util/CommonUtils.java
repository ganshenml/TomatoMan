package com.example.ganshenml.tomatoman.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Vibrator;

import com.example.ganshenml.tomatoman.R;

/**
 * Created by ganshenml on 2016/4/23.
 * 常用的工具类
 */

public class CommonUtils {

    //计时任务自动完成时的振动方法
    public static void startVibrate(Context context) {
        long[] patterns = new long[]{0, 1000, 2000, 1000, 2000, 1000};
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(patterns, -1);//不重复，仅一次
    }

    /**
     * 获取当前APP的版本号
     *
     * @param context
     * @return
     */
    public static String getCurrentAppVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return context.getString(R.string.can_not_find_version_name);
        }
    }
}
