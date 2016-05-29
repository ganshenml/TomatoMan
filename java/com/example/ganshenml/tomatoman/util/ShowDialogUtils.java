package com.example.ganshenml.tomatoman.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

/**
 * Created by ganshenml on 2016/4/17.
 * 根据传入的参数显示相应的弹窗
 */
public class ShowDialogUtils {

    //点击回退键时弹出弹窗（番茄计时相关页面用到）
    public static void showDialog(final Context packageContext, final Class<?> cls, final Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(packageContext);
        builder.setTitle("提示").setMessage("确定终止当前任务！").setCancelable(true)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//点击确定：不保存当前数据，回到首页
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//点击确认：1.跳转至“番茄完成页”2.传递当前统计数据
                        Intent intent = new Intent(packageContext,cls);
                        activity.finish();
                        activity.startActivity(intent);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}
