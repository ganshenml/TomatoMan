package com.example.ganshenml.tomatoman.tool;

/**
 * Created by ganshenml on 2016-07-19.
 */

import android.util.Log;
/**
 * 调试打印信息（分配每一个开发人员）
 */
public class LogTool {

    // log输出开关，发布的时候设成false
    public static boolean DEBUG = false;
    public static final String Aaron = "aaron";


    /**
     * 平常输出Log用
     *
     * @param tag
     * @param msg
     */
    public static void log(String tag, String msg) {
        if (DEBUG)
            Log.d(tag, msg);
    }

    /**
     * 用以出现输出错误的log
     * @param tag
     * @param msg
     */
    public static void error(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void error(String tag, String msg, Throwable t) {
        if (DEBUG) {
            Log.e(tag, msg, t);
        }
    }


}
