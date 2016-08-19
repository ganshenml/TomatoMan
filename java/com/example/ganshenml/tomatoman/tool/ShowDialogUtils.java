package com.example.ganshenml.tomatoman.tool;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.callback.HttpCallback;

/**
 * Created by ganshenml on 2016/4/17.
 * 根据传入的参数显示相应的弹窗
 */
public class ShowDialogUtils {

    //点击回退键时弹出弹窗（番茄计时相关页面用到）
    public static void showDialog(final Context packageContext, final Class<?> cls, final AppCompatActivity activity) {
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
                        Intent intent = new Intent(packageContext, cls);
                        activity.finish();
                        activity.startActivity(intent);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    /**
     * 显示编辑简介的弹窗
     */
    public static void showInputTextDialog(Context packageContext, String titleStr, String userIntroStr, final HttpCallback httpCallback) {

        View view = LayoutInflater.from(packageContext).inflate(R.layout.dialog_edittext, null);
        final EditText userIntroEt = (EditText) view.findViewById(R.id.userIntroEt);
        final TextView titleTv = (TextView) view.findViewById(R.id.titleTv);
        if (!StringTool.isEmpty(userIntroStr)) {
            userIntroEt.setText(userIntroStr);
            userIntroEt.setSelection(userIntroEt.length());
        }

        if (titleStr != null) {
            titleTv.setText(titleStr);
        }

        new AlertDialog.Builder(packageContext)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        httpCallback.onSuccess(null, userIntroEt.getText().toString());//回调输入的内容
                    }
                })
                .setView(view)
                .create()
                .show();
    }

    /**
     * 显示点击个人主页-用户头像后的单选项：查看大图、拍照、上传图片
     */
    public static void showLogoItem(final Context packageContext, final HttpCallback httpCallback) {
        String[] itemsStr = new String[]{"从相册选择", "拍照"};
        new AlertDialog.Builder(packageContext)
                .setSingleChoiceItems(itemsStr, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        httpCallback.onSuccess(null, "" + (which + 1));
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    /**
     * 显示文字提示的弹窗
     *
     * @param packageContext
     * @param hintStr
     * @param httpCallback
     */
    public static void showSimpleHintDialog(Context packageContext, String hintStr, final HttpCallback httpCallback) {
        LogTool.log(LogTool.Aaron, "sdf23234");
        new AlertDialog.Builder(packageContext)
                .setMessage(hintStr)
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogTool.log(LogTool.Aaron, "点击了确认");
                        httpCallback.onComplete(null);
                    }
                })
                .create()
                .show();
    }

}
