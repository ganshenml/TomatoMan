package com.example.ganshenml.tomatoman.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.ganshenml.tomatoman.act.TomatoCountTimeAct;
import com.example.ganshenml.tomatoman.act.TomatoEfficiencyAct;
import com.example.ganshenml.tomatoman.act.TomatoRestAct;
import com.example.ganshenml.tomatoman.act.TomatoTemporaryAct;
import com.example.ganshenml.tomatoman.bean.data.StaticData;
import com.example.ganshenml.tomatoman.tool.CommonUtils;
import com.example.ganshenml.tomatoman.tool.ContextManager;
import com.example.ganshenml.tomatoman.tool.LogTool;
import com.example.ganshenml.tomatoman.tool.SpTool;

/**
 * Created by ganshenml on 2016/4/22.
 * 处理计时任务到点时的通知：1.一般用来进行Activity的跳转;2.对当前计时数据的文件保存;3.根据流程来调用振动方法
 */
public class TimeArriveReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {//1.将数据（一个工作番茄or一个休息时间or一个高效时间）写入文件；2.finish当前activity，并跳转至下一个activity
        Log.e("onReceive", "进入了");

        //从ContextManager的工具类中获取当前正在使用的Activity
        Context myContext = ContextManager.getCurrentRunningContext();

        if (myContext instanceof TomatoCountTimeAct) {//如果是TomatoCountTimeAct
            LogTool.log(LogTool.Aaron, "TimeArriveReceiver 进入了TomatoCountTimeAct的判断逻辑");

            //1.finish当前activity
            ((TomatoCountTimeAct) myContext).finish();

            //2.进行下一个activity的跳转
            Intent intent1 = new Intent(myContext, TomatoTemporaryAct.class);//跳转至“中间临时页”
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);

            //3.写数据
            //更新sp中番茄数量
            int tomatoCompletedNumTemp = SpTool.getInt(StaticData.SPTOMATOCOMPLETENUM, 0);
            tomatoCompletedNumTemp++;
            SpTool.putInt(StaticData.SPTOMATOCOMPLETENUM, tomatoCompletedNumTemp);

        } else if (myContext instanceof TomatoRestAct) {
            LogTool.log(LogTool.Aaron, "TimeArriveReceiver 进入了TomatoRestAct的判断逻辑");
            //1.finish当前activity
            ((TomatoRestAct) myContext).finish();

            //2.进行下一个activity的跳转
            Intent intent1 = new Intent(myContext, TomatoTemporaryAct.class);//跳转至“中间临时页”
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);

            //3.写数据
            int completeTime = intent.getIntExtra("completeTime", 0);
            if (completeTime > 0) {//以免用户设置工作时间为0带来的不必要的数据写入
                //开始向文件写数据

            }
        } else if (myContext instanceof TomatoEfficiencyAct) {
            LogTool.log(LogTool.Aaron, "TimeArriveReceiver 进入了TomatoEfficiencyAct的判断逻辑");

            //1.finish当前activity
            ((TomatoEfficiencyAct) myContext).finish();

            //2.进行下一个activity的跳转
            Intent intent1 = new Intent(myContext, TomatoTemporaryAct.class);//跳转至“中间临时页”
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);

            //3.写数据
            int completeTime = intent.getIntExtra("completeTime", 0);
            if (completeTime > 0) {//以免用户设置工作时间为0带来的不必要的数据写入
                //开始向文件写数据

            }
        }

        //获取振动的参数:如果是振动，则调用振动工具类方法
        if (SpTool.getBoolean(StaticData.SPVIBRATEALARM, true)) {
            CommonUtils.startVibrate(context);
        }

    }
}
