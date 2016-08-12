package com.example.ganshenml.tomatoman.view;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.tool.ThreadTool;


/**
 * 请求网络提示框
 */
public class WebProgress extends Dialog {

    private int cancelTime = 4000;

    private static WebProgress instance = null;

    private static Object locker = new Object();

//    private Http http;

    public interface  WebProgressCallback {

        void callback();

    }



    public WebProgress(Context context) {
        super(context, R.style.CustomProgressDialog);
        setContentView(R.layout.web_progress_dialog);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        findViewById(R.id.loadingIv).startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_animation));
        setCancelable(false);
        final Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webDismiss();//Aaron_7.7
                dismiss();
            }
        });
        ThreadTool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    cancelBtn.setVisibility(View.VISIBLE);
            }
        }, cancelTime);
        show();
    }

//    public WebProgress(Context context, final Http http) {
//        super(context, R.style.CustomProgressDialog);
//        setContentView(R.layout.web_progress_dialog);
//        getWindow().getAttributes().gravity = Gravity.CENTER;
//        findViewById(R.id.loadingIv).startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_animation));
//        setCancelable(false);
//        this.http = http;
//        final Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (http != null)
//                    http.cancelHttp();
//                webDismiss();//Aaron_7.7
//                dismiss();
//            }
//        });
//        ThreadTool.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (instance != null && instance.http == http) {
//                    cancelBtn.setVisibility(View.VISIBLE);
//                }
//            }
//        }, cancelTime);
//        show();
//    }
//

    public WebProgress(Context context, int time, final WebProgressCallback callback) {
        super(context, R.style.CustomProgressDialog);
        Log.e("WebProgress_构造","进入了设置");//Aaron_log_5.28
        setContentView(R.layout.web_progress_dialog);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        findViewById(R.id.loadingIv).startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_animation));
        setCancelable(false);
        final Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callback();

                dismiss();
            }
        });
        ThreadTool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (instance != null) {
                    cancelBtn.setVisibility(View.VISIBLE);
                }
            }
        }, time);
        show();
        Log.e("WebProgress_构造","执行了show()");

    }


    /**
     * 创建一个进度框
     *
     * @return
     */
    public static WebProgress createOtaDialog(Context context) {
        synchronized (locker) {
            webDismiss();
            instance = new WebProgress(context);
        }
        return instance;
    }



    /**
     * 检测对话框是否有效
     * @return
     */
    public static boolean inValidInstance(){
        return instance == null;

    }


    public static WebProgress createDialog(Context context) {
        synchronized (locker) {
            webDismiss();
            instance = new WebProgress(context);
        }
        return instance;
    }



    public static void webDismiss(){
        synchronized (locker){
            if(instance != null){
                instance.dismiss();
                instance = null;
            }
        }
    }

    public static void setLoadingStr(final String str) {
        if (instance != null) {
            ThreadTool.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView tv = (TextView) instance.findViewById(R.id.loadingStrTv);
                    tv.setText(str);
                }
            });
        }
    }

    public static void setLoadingStr(int resId) {
        if (instance != null) {
            TextView tv = (TextView) instance.findViewById(R.id.loadingStrTv);
            tv.setText(resId);
        }
    }


}
