package com.example.ganshenml.tomatoman.tool;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by cyan on 15/9/21.
 * <p/>
 * 保存所有的sharePreferences 信息,并且建议key 可保存在当前的类中。
 * key直接定义在当前的界面
 */
public class SpTool {

    public static SharedPreferences config;
    public static SharedPreferences.Editor editor;

    public static void init(Context c) {
        config = c.getSharedPreferences("TomaotSetting", Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor() {
        if (editor == null) {
            editor = config.edit();
        }
        return editor;
    }

    /**
     * 清除所有键，但保留用户名，注销时使用;
     * 也保留上一次用户选择公司的companyId（方便下次进入时直接获取显示）_Aaron_6.22
     */
    public static void clearAllData() {
    }


    public static void putString(String key, String value) {
        getEditor().putString(key, value).commit();
    }

    public static void putInt(String key, int value) {
        getEditor().putInt(key, value).commit();
    }

    public static void putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).commit();
    }

    public static void putFloat(String key, float value) {
        getEditor().putFloat(key, value).commit();
    }

    public static void putLong(String key, long value) {
        getEditor().putLong(key, value).commit();

    }


    public static String getString(String key, String defaultValue) {
        return config.getString(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return config.getInt(key, defaultValue);
    }

    public static long getLong(String key, int defaultValue) {
        return config.getLong(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean flag) {
        return config.getBoolean(key, flag);
    }


}
