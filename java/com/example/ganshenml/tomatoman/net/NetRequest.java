package com.example.ganshenml.tomatoman.net;

import com.example.ganshenml.tomatoman.bean.Person;
import com.example.ganshenml.tomatoman.bean.TomatoRecord;
import com.example.ganshenml.tomatoman.bean.data.PushMessage;
import com.example.ganshenml.tomatoman.callback.HttpCallback;
import com.example.ganshenml.tomatoman.tool.CommonUtils;
import com.example.ganshenml.tomatoman.tool.LogTool;
import com.example.ganshenml.tomatoman.tool.StringTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * 网络请求的方法类
 * Created by ganshenml on 2016-07-29.
 */
public class NetRequest {

    /**
     * （当本地TomatoRecord数据为空时，从服务器端取100条）
     *
     * @return
     */
    public static void requestWholeTomatoRecordData(final HttpCallback<TomatoRecord> httpCallback) {

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
     *
     * @param httpCallback
     */
    public static void requestTomatoRecordLatestData(final String dateStr, final HttpCallback<TomatoRecord> httpCallback) {

        if (StringTool.isEmpty(dateStr)) {
            return;
        }

        BmobUser currentPerson = Person.getCurrentUser();
        BmobQuery<TomatoRecord> query = new BmobQuery<TomatoRecord>();

        List<BmobQuery<TomatoRecord>> and = new ArrayList<BmobQuery<TomatoRecord>>();

        //添加时间条件
        BmobQuery<TomatoRecord> q1 = new BmobQuery<TomatoRecord>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThan("createdAt", new BmobDate(date));
        and.add(q1);

        //添加用户条件
        BmobQuery<TomatoRecord> q2 = new BmobQuery<TomatoRecord>();
        q2.addWhereEqualTo("person", currentPerson);
        and.add(q2);

        //添加复合与查询
        query.and(and);

        //进行查询
        query.findObjects(new FindListener<TomatoRecord>() {
            @Override
            public void done(List<TomatoRecord> list, BmobException e) {
                if (StringTool.hasData(list)) {
                    if (list.get(0).getCreatedAt().equals(dateStr)) {//如果当前日期是相同，则去除该数据（Bmob数据日期存储少一秒的原因导致查询结果会包含一条重复的数据）
                        list.remove(0);
                    }
                    int length = list.size();

                    if (list.get(length - 1).getCreatedAt().equals(dateStr)) {
                        list.remove(length - 1);
                    }
                }

                httpCallback.onSuccess(list);

            }
        });
    }

    /**
     * 返回该用户所完成的番茄个数
     *
     * @param person
     * @return
     */
    public static void returnTomatoNumData(Person person, final HttpCallback httpCallback) {
        BmobQuery<TomatoRecord> query = new BmobQuery<TomatoRecord>();
        query.sum(new String[]{"tomatoNum"});
        query.addWhereEqualTo("person", person);
        query.findStatistics(TomatoRecord.class, new QueryListener<JSONArray>() {

            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {
                    if (ary != null) {
                        try {
                            LogTool.log(LogTool.Aaron, "returnTomatoNumData " + ary.toString());

                            JSONObject obj = ary.getJSONObject(0);
                            int sum = obj.getInt("_sumTomatoNum");//_(关键字)+首字母大写的列名

                            httpCallback.onSuccess(null, sum + "");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else {
                    LogTool.log(LogTool.Aaron, "returnTomatoNumData 失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });
    }

    /**
     * 返回该用户所完成的番茄总时间
     *
     * @param person
     * @return
     */
    public static void returnTomatoTimeData(Person person, final HttpCallback httpCallback) {
        BmobQuery<TomatoRecord> query = new BmobQuery<TomatoRecord>();
        query.sum(new String[]{"tomatoTime"});
        query.addWhereEqualTo("person", person);
        query.findStatistics(TomatoRecord.class, new QueryListener<JSONArray>() {

            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {
                    if (ary != null) {
                        try {
                            JSONObject obj = ary.getJSONObject(0);
                            int sum = obj.getInt("_sumTomatoTime");//_(关键字)+首字母大写的列名
                            LogTool.log(LogTool.Aaron, "returnTomatoTimeData " + sum);
                            httpCallback.onSuccess(null, CommonUtils.toOnePointDecimal(sum / 60.0) + "");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else {
                    LogTool.log(LogTool.Aaron, "returnTomatoTimeData 失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });
    }

    /**
     * 返回该用户所完成的番茄总时间
     *
     * @param person
     * @return
     */
    public static void returnEfficientTimeData(Person person, final HttpCallback httpCallback) {
        BmobQuery<TomatoRecord> query = new BmobQuery<TomatoRecord>();
        query.sum(new String[]{"efficientTime"});
        query.addWhereEqualTo("person", person);
        query.findStatistics(TomatoRecord.class, new QueryListener<JSONArray>() {

            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {
                    if (ary != null) {
                        try {
                            JSONObject obj = ary.getJSONObject(0);
                            int sum = obj.getInt("_sumEfficientTime");//_(关键字)+首字母大写的列名
                            LogTool.log(LogTool.Aaron, "returnEfficientTimeData " + sum);
                            httpCallback.onSuccess(null, CommonUtils.toOnePointDecimal(sum / 60.0) + "");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else {
                    LogTool.log(LogTool.Aaron, "returnefficientTimeData 失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });
    }

    public static void requestLatestTomatoRecordCreatedAtData(Person person, final HttpCallback httpCallback) {
        BmobQuery<TomatoRecord> query = new BmobQuery<TomatoRecord>();
        query.max(new String[]{"createdAt"});//查询最大值
        query.groupby(new String[]{"createdAt"});
        query.addWhereEqualTo("person", person);
        query.findStatistics(TomatoRecord.class, new QueryListener<JSONArray>() {

            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {
                    JSONArray aryTemp = (JSONArray) ary;
                    if (ary != null) {
                        try {
                            JSONObject obj = aryTemp.getJSONObject(0);
                            String createdAt = obj.getString("createdAt");
                            httpCallback.onSuccess(null, createdAt);
                        } catch (JSONException et) {
                            et.printStackTrace();
                            httpCallback.onSuccess(null, null);
                        }
                    } else {
                        httpCallback.onSuccess(null, null);
                        LogTool.log(LogTool.Aaron, "NetRequest 查询最大CreatedAt数据结果： 查询成功，无数据");
                    }
                } else {
                    httpCallback.onFailure(0, null);
                    LogTool.log(LogTool.Aaron, "NetRequest 查询最大CreatedAt数据结果： " + e.toString());
                }
            }
        });
    }

    /**
     * 分页查询TomatoRecord数据(20)
     */
    public static void requestTomatoRecordByPage(final Person person, int skipNum, final HttpCallback<TomatoRecord> httpCallback) {
        BmobQuery<TomatoRecord> query = new BmobQuery<TomatoRecord>();
        query.addWhereEqualTo("person", person);
        query.groupby(new String[]{"createdAt"});
        query.order("-createdAt");//倒序
        query.setSkip(skipNum);
        query.setLimit(20);

        query.findObjects(new FindListener<TomatoRecord>() {
            @Override
            public void done(List<TomatoRecord> list, BmobException e) {
                if (e == null) {
                    httpCallback.onSuccess(list);
                } else {
                    httpCallback.onFailure(0, null);
                    LogTool.log(LogTool.Aaron, "NetRequest 查询最大CreatedAt数据结果： " + e.toString());
                }
            }
        });

    }

    public static void returnLatestMessage(final HttpCallback<PushMessage> httpCallback){
        String bql = "select * from PushMessage order by -createdAt";
        BmobQuery<PushMessage> query=new BmobQuery<PushMessage>();
        query.setSQL(bql);
        query.doSQLQuery(new SQLQueryListener<PushMessage>(){
            @Override
            public void done(BmobQueryResult<PushMessage> result, BmobException e) {
                if(e ==null){
                    List<PushMessage> list = (List<PushMessage>) result.getResults();
                        httpCallback.onSuccess(list);
                }else{
                    LogTool.log(LogTool.Aaron, "查询最新的push消息 错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        });
    }
}
