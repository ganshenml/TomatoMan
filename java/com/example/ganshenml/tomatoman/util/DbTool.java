package com.example.ganshenml.tomatoman.util;

import android.content.ContentValues;

import com.example.ganshenml.tomatoman.bean.Extra;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * 数据库工具类，这里后期需要加入当用户切换了的时候，那么必须清除数据库中的数据
 */
public class DbTool {

    /**
     * 清除所有数据库中表的数据
     */
    public static void clearDb() {
        DataSupport.deleteAll(Extra.class);
    }

    /**
     * 保存Extra表数据至本地
     */
    public static void saveExtraData(Extra extra) {
        Extra extraTemp = DataSupport.findFirst(Extra.class);
        if (extraTemp != null) {
            extraTemp.delete();
        }
        extra.save();
    }

    /**
     * 返回本地保存的Extra数据
     * @return
     */
    public static Extra findLocalExtraData(){
        Extra extra = DataSupport.findFirst(Extra.class);
        return extra;
    }
}