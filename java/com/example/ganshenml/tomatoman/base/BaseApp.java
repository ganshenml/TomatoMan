package com.example.ganshenml.tomatoman.base;

import android.graphics.Color;
import org.litepal.LitePalApplication;


public class BaseApp extends LitePalApplication {

    private static BaseApp sInstance;

    public static int COLOR ;

    public static BaseApp getInstance() {
        if(sInstance == null){
            sInstance = new BaseApp();
        }
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }


    private void init() {
        sInstance = this;
        COLOR = Color.parseColor("#ff8a00");
        LitePalApplication.initialize(sInstance);
//        CrashHandler.getInstance().init();
//        SpTool.init(sInstance);
//        ScreenTool.init(sInstance);

    }


    public static String findResIdDecoderString(int resId){
        return sInstance.getResources().getString(resId);
    }


    public static String[] findResIdDecoderArray(int resId){
        return sInstance.getResources().getStringArray(resId);
    }



}
