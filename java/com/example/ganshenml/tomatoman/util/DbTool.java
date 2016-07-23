package com.example.ganshenml.tomatoman.util;

import android.content.ContentValues;

import com.example.ganshenml.tomatoman.bean.Extra;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

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
            LogTool.log(LogTool.Aaron," saveExtraData extraTemp不为空");
            DataSupport.deleteAll(Extra.class);
        }
        extra.save();
        LogTool.log(LogTool.Aaron," saveExtraData extra 的值： "+ extra.getFeedbackHint());
    }

    /**
     * 返回本地保存的Extra数据
     * @return
     */
    public static Extra findLocalExtraData(){
        List<Extra> extraList = DataSupport.findAll(Extra.class);
        if(StringTool.hasData(extraList)){
            LogTool.log(LogTool.Aaron," DbTool findLocalExtraData Extra的大小是： "+extraList.size() );
            return extraList.get(0);
        }
        LogTool.log(LogTool.Aaron," DbTool findLocalExtraData Extra的大小是： "+0 );
        return null;
    }
}
