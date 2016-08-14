package com.example.ganshenml.tomatoman.tool;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.Person;
import com.example.ganshenml.tomatoman.bean.TomatoRecord;
import com.example.ganshenml.tomatoman.bean.beant.TomatoRecordT;
import com.example.ganshenml.tomatoman.bean.data.StaticData;
import com.example.ganshenml.tomatoman.net.WifiOperate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;

/**
 * Created by ganshenml on 2016/4/23.
 * 常用的工具类
 */

public class CommonUtils {

    //计时任务自动完成时的振动方法
    public static void startVibrate(Context context) {
        long[] patterns = new long[]{0, 1000, 2000, 1000, 2000, 1000};
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(patterns, -1);//不重复，仅一次
    }

    //计时任务自动完成时的振动方法
    public static void startStampVibrator(Context context) {
        final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        ThreadTool.runOnNewThread(new Runnable() {
            @Override
            public void run() {
                vibrator.vibrate(60);
            }
        },1100);
    }

    /**
     * 获取当前APP的版本号
     *
     * @param context
     * @return
     */
    public static String getCurrentAppVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return context.getString(R.string.can_not_find_version_name);
        }
    }

    /**
     * 获得当前的系统时间
     *
     * @return
     */
    public static String getCurrentDataAndTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    /**
     * 获得当前系统时间所属的星期
     */

    public static String getCurrentWeek() {
        Date date = new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        return dateFm.format(date);
    }

    /**
     * 构造需要保存的TomatoRecord对象
     *
     * @return
     */
    public static TomatoRecord constructUploadObject(String tomatoNoteStr, int evaluateLever) {
        String taskNameTemp;//任务名称
        Person personTemp;//用户
        String taskTimeTemp;//任务完成的时间
        String weekTemp;//任务完成所属星期
        String completeStateTemp;//完成状态：已完成、未完成
        Integer tomatoNumTemp = 0;//获得的番茄数
        Integer tomatoTimeTemp = 0; //获得的番茄时间

        String efficientTimeTemp;//高效时间：无或者以分钟计算
        String tomatoNoteTemp;//备注
        Integer evaluateLeverTemp = 0;//评价：1,2,3,4,5分（0分表示未评分，默认）

        TomatoRecord tomatoRecordTemp = new TomatoRecord();
        taskNameTemp = SpTool.getString(StaticData.SPTASKNAME, "");
        personTemp = (Person) Person.getCurrentUser();
        taskTimeTemp = CommonUtils.getCurrentDataAndTime();
        weekTemp = CommonUtils.getCurrentWeek();

        tomatoNumTemp = SpTool.getInt(StaticData.SPTOMATOCOMPLETENUM, 0);
        tomatoTimeTemp = SpTool.getInt(StaticData.SPWORKTIME, 0);
        efficientTimeTemp = SpTool.getInt(StaticData.SPTOMATOCOMPLETEEFFICIENTTIME, 0) + "分钟";
        tomatoNoteTemp = tomatoNoteStr;
        evaluateLeverTemp = evaluateLever;

        tomatoRecordTemp.setTaskName(taskNameTemp);
        tomatoRecordTemp.setPerson(personTemp);
        tomatoRecordTemp.setTaskTime(taskTimeTemp);
        tomatoRecordTemp.setWeek(weekTemp);
        //"完成状态"目前空缺..........................

        tomatoRecordTemp.setTomatoNum(tomatoNumTemp);
        tomatoRecordTemp.setTomatoTime(tomatoTimeTemp);
        tomatoRecordTemp.setEfficientTime(efficientTimeTemp);
        tomatoRecordTemp.setTomatoNote(tomatoNoteTemp);
        tomatoRecordTemp.setEvaluateLever(evaluateLeverTemp);

        return tomatoRecordTemp;
    }


    /**
     * 重置相关sp数据（番茄数，番茄时间）
     */
    public static void resetSpData() {
        SpTool.putInt(StaticData.SPTOMATOCOMPLETENUM, 0);
        SpTool.putInt(StaticData.SPWORKTIME, 0);
        SpTool.putString(StaticData.SPTASKNAME, null);
    }

    /**
     * 将本地未上传的TomatoRecordT数据进行上传，返回成功后并更新本地数据的objectId和createdAt字段
     */
    public static void uploadRestTomatoRecordTData() {
        //构造上传的数据
        List<TomatoRecordT> tomatoRecordTs = DbTool.returnAllNotUploadedTomatoRecordTData();
        final List<BmobObject> tomatoRecordList = new ArrayList<>();
        int sizeTemp = tomatoRecordTs.size();
        for (int i = 0; i < sizeTemp; i++) {
            TomatoRecord t = new TomatoRecord(tomatoRecordTs.get(i));
            tomatoRecordList.add(t);
        }

        //开始上传，成功后并同步本地数据
        new BmobBatch().insertBatch(tomatoRecordList).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < o.size(); i++) {
                        BatchResult result = o.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            LogTool.log(LogTool.Aaron,"第" + i + "个数据批量添加成功：" + result.getCreatedAt() + "," + result.getObjectId() + "," + result.getUpdatedAt());
                            DbTool.update_CreatedAt_InLocal((TomatoRecord)tomatoRecordList.get(i));
                        } else {
                            LogTool.log(LogTool.Aaron,"第" + i + "个数据批量添加失败：" + ex.getMessage() + "," + ex.getErrorCode());
                        }
                    }
                } else {
                    LogTool.log(LogTool.Aaron,"失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }

    /**
     * 判断当前网络是否可用
     * @param context
     * @return
     */
    public static boolean judgeNetWork(Context context) {
        WifiOperate wifiOperate = new WifiOperate(context);
        if (!wifiOperate.isNetworkConnected()) {
            Toast.makeText(context, "当前未开启网络", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}