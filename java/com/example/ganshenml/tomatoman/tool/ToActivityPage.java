package com.example.ganshenml.tomatoman.tool;

import android.content.Context;
import android.content.Intent;

/**
 * Created by ganshenml on 2016-07-20.
 */
public class ToActivityPage {

    /**
     *  跳转至下一个Activity页面（不带参数）
     * @param packageContext
     * @param cls
     */
    public static void turnToSimpleAct(Context packageContext, Class<?> cls){
        Intent intent = new Intent(packageContext,cls);
        packageContext.startActivity(intent);
    }

    /**
     * 跳转至指定的Activity（一般是声明实例化目标Activity对象，主要是用来传递参数）
     * @param packageContext
     * @param targetAct
     */
    public static void turnToTargetAct(Context packageContext , Context targetAct){
        Intent intent = new Intent(packageContext,targetAct.getClass());
        packageContext.startActivity(intent);

    }
}
