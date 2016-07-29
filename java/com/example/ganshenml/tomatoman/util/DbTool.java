package com.example.ganshenml.tomatoman.util;

import android.content.ContentValues;

import com.example.ganshenml.tomatoman.bean.Extra;
import com.example.ganshenml.tomatoman.bean.TomatoRecord;
import com.example.ganshenml.tomatoman.bean.beant.ExtraT;
import com.example.ganshenml.tomatoman.bean.beant.TomatoRecordT;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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
        ExtraT extraT = new ExtraT(extra);
        ExtraT extraTTemp = DataSupport.findFirst(ExtraT.class);
        if (extraTTemp != null) {
            LogTool.log(LogTool.Aaron," saveExtraData extraTemp不为空");
            DataSupport.deleteAll(ExtraT.class);
        }
        LogTool.log(LogTool.Aaron," saveExtraData extra 的值： "+ extra.getFeedbackHint());
        extraT.save();
    }

    /**
     * 返回本地保存的Extra数据
     * @return
     */
    public static ExtraT findLocalExtraData(){
        List<ExtraT> extraTList = DataSupport.findAll(ExtraT.class);
        if(StringTool.hasData(extraTList)){
            LogTool.log(LogTool.Aaron," DbTool findLocalExtraData Extra的大小是： "+extraTList.size() );
            return extraTList.get(0);
        }
        LogTool.log(LogTool.Aaron," DbTool findLocalExtraData Extra的大小是： "+0 );
        return null;
    }


    /**
     * 请求Extra数据并保存至本地
     */
    public static void upDataExtraData() {
        BmobQuery<Extra> extraBmobQuery = new BmobQuery<>();
        extraBmobQuery.findObjects(new FindListener<Extra>() {
            @Override
            public void done(List<Extra> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        DbTool.saveExtraData(list.get(0));
                        LogTool.log(LogTool.Aaron, "DbTool   upDataExtraData 查询到了数据并进行本地保存");

                    }
                } else {
                    LogTool.log(LogTool.Aaron,"DbTool  upDataExtraData 查询Extra数据出错： " + e.toString());
                }
            }
        });
    }

    /**
     * 判断本地是否存有TomatoRecord数据
     * @return
     */
    public static boolean isHasTomatoRecordData(){
       TomatoRecordT tomatoRecordT = DataSupport.findFirst(TomatoRecordT.class);
        if(tomatoRecordT!=null){
            return true;
        }
        return false;
    }

    /**
     * 保存TomatoRecord数据至本地数据库
     */
    public static void saveTomatoRecordToLocal(List<TomatoRecord> list){
        List<TomatoRecordT> listTemp = new ArrayList<>() ;
        int length = list.size();
        for (int i = 0; i < length; i++) {
            TomatoRecordT tomatoRecordT = new TomatoRecordT(list.get(i));
            listTemp.add(tomatoRecordT);
        }

        DataSupport.saveAll(listTemp);
    }

    /**
     * 返回最新的TomatoRecord的单条本地数据
     * @return
     */
    public static TomatoRecordT returnLatestTomatoRecordData(){
        return DataSupport.findFirst(TomatoRecordT.class);
    }

    /**
     * 返回本地保存的所有TomatoRecordT数据
     * @return
     */
    public static List<TomatoRecordT> returnWholeTomatoRecordData(){
        return DataSupport.findAll(TomatoRecordT.class);
    }
}














