package com.example.ganshenml.tomatoman.net;

import com.example.ganshenml.tomatoman.bean.Person;
import com.example.ganshenml.tomatoman.bean.TomatoRecord;
import com.example.ganshenml.tomatoman.callback.HttpCallback;
import com.example.ganshenml.tomatoman.util.StringTool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 网络请求的方法类
 * Created by ganshenml on 2016-07-29.
 */
public class NetRequest {

    /**
     *（当本地TomatoRecord数据为空时，从服务器端取100条）
     * @return
     */
    public static void  requestWholeTomatoRecordData(final HttpCallback<TomatoRecord> httpCallback){

        BmobQuery<TomatoRecord> tomatoRecordBmobQuery = new BmobQuery<TomatoRecord>();
        tomatoRecordBmobQuery.addWhereEqualTo("person", BmobUser.getCurrentUser());
        tomatoRecordBmobQuery.setLimit(100);
        tomatoRecordBmobQuery.findObjects(new FindListener<TomatoRecord>() {
            @Override
            public void done(List<TomatoRecord> list, BmobException e) {
                httpCallback.onSuccess(list);
            }
        });
    }

    /**
     * 取出相较于本地数据获取最新的TomatoRecord数据（有可能也是超过100条的，暂不做处理）
     * @param httpCallback
     */
    public static void requestTomatoRecordLatestData(String dateStr ,final HttpCallback<TomatoRecord> httpCallback){

        if(StringTool.isEmpty(dateStr)){
            return;
        }

        BmobUser currentPerson =  Person.getCurrentUser();
        BmobQuery<TomatoRecord> query = new BmobQuery<TomatoRecord>();

        List<BmobQuery<TomatoRecord>> and = new ArrayList<BmobQuery<TomatoRecord>>();

        //添加时间条件
        BmobQuery<TomatoRecord> q1 = new BmobQuery<TomatoRecord>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date  = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThan("createdAt",new BmobDate(date));
        and.add(q1);

        //添加用户条件
        BmobQuery<TomatoRecord> q2 = new BmobQuery<TomatoRecord>();
        q2.addWhereEqualTo("person",currentPerson);
        and.add(q2);

        //添加复合与查询
        query.and(and);

        //进行查询
        query.findObjects(new FindListener<TomatoRecord>() {
            @Override
            public void done(List<TomatoRecord> list, BmobException e) {
                httpCallback.onSuccess(list);
            }
        });
    }
}
