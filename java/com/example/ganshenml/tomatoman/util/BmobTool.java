package com.example.ganshenml.tomatoman.util;

import android.content.Context;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by ganshenml on 2016-07-19.
 */
public class BmobTool {

    public static void init(Context context) {
        BmobConfig config = new BmobConfig.Builder(context)
                .setApplicationId(ConstantCode.ApplicationID)
                .setConnectTimeout(30)
                .setUploadBlockSize(1024 * 1024)
                .setFileExpiration(2500)
                .build();

        Bmob.initialize(config);
    }
}
