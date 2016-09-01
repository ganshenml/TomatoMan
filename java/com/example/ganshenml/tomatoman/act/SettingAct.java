package com.example.ganshenml.tomatoman.act;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.FeedBack;
import com.example.ganshenml.tomatoman.bean.Person;
import com.example.ganshenml.tomatoman.bean.beant.ExtraT;
import com.example.ganshenml.tomatoman.callback.HttpCallback;
import com.example.ganshenml.tomatoman.tool.CommonUtils;
import com.example.ganshenml.tomatoman.tool.ContextManager;
import com.example.ganshenml.tomatoman.tool.DbTool;
import com.example.ganshenml.tomatoman.tool.LogTool;
import com.example.ganshenml.tomatoman.tool.ShowDialogUtils;
import com.example.ganshenml.tomatoman.tool.ToActivityPage;
import com.example.ganshenml.tomatoman.view.WebProgress;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.update.BmobUpdateAgent;

public class SettingAct extends BaseActivity {
    private LinearLayout llCallback,appVersionLl, logoutLl,tomatoTimerLl;
    private TextView appVersionTv;
    private EditText feedbackEt;
    private Button feedbackBtn;
    private ImageView newAppVersionIv, backIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_act);
        initViews();
        initDataViews();
        initListeners();

    }


    private void initViews() {
        appVersionTv = (TextView) findViewById(R.id.appVersionTv);
        newAppVersionIv = (ImageView) findViewById(R.id.newAppVersionIv);
        llCallback = (LinearLayout) findViewById(R.id.llCallback);
        appVersionLl = (LinearLayout) findViewById(R.id.appVersionLl);
        logoutLl = (LinearLayout) findViewById(R.id.logoutLl);
        tomatoTimerLl = (LinearLayout) findViewById(R.id.tomatoTimerLl);
        feedbackEt = (EditText) findViewById(R.id.feedbackEt);
        feedbackBtn = (Button) findViewById(R.id.feedbackBtn);
        backIv = (ImageView) findViewById(R.id.backIv);
    }

    private void initDataViews() {
//        BmobUpdateAgent.initAppVersion();//初始化appVersion表
        BmobUpdateAgent.update(this);//自动更新弹窗（WiFi网络下）

        String appVersionStr = CommonUtils.getCurrentAppVersion(this);
        appVersionTv.setText(getResources().getText(R.string.app_version) + " " + appVersionStr);

        ExtraT extraTTemp = DbTool.findLocalExtraData();//返回本地存储的Extra数据
        if (extraTTemp != null) {
            LogTool.log(LogTool.Aaron, " settingFragment initDataViews　本地存储的Extra数据不为空");
            feedbackEt.setHint(extraTTemp.getFeedbackHint());

            if (extraTTemp.getAppVersion().compareToIgnoreCase(appVersionStr) > 0) {
                newAppVersionIv.setVisibility(View.VISIBLE);
            }
        }

//        if(Person.getCurrentUser()==null){
//            logoutLl.setVisibility(View.GONE);
//        }

    }

    private void initListeners() {

        llCallback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CommonUtils.judgeNetWork(SettingAct.this)){//如果当前网络不可用
                    return;
                }

                ShowDialogUtils.showInputTextDialog(SettingAct.this, "填写反馈信息", "", new HttpCallback() {
                    @Override
                    public void onSuccess(Object data, String resultStr) {
                        uploadFeedbackData(resultStr);
                    }
                });
            }
        });

        appVersionLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ShowDialogUtils.showSimpleHintDialog(SettingAct.this, "是否下载新版本？", new HttpCallback() {
//                    @Override
//                    public void onComplete(Object data) {
//                        //进行下载操作
//                    }
//                });

                BmobUpdateAgent.forceUpdate(SettingAct.this);//检查更新（如果有可以更新，则弹窗提示更新）
            }
        });

        logoutLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //注销账号弹窗
                ShowDialogUtils.showSimpleHintDialog(SettingAct.this, "确定退出该账号？", new HttpCallback() {
                    @Override
                    public void onComplete(Object data) {
                        BmobUser.logOut();
                        DbTool.clearDb();
                        ContextManager.finishAndRemoveAllContext();
                        ToActivityPage.turnToSimpleAct(SettingAct.this, LoginAct.class);
                        finish();
                    }
                });
            }
        });

        tomatoTimerLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToActivityPage.turnToSimpleAct(SettingAct.this,TomatoSettingAct.class);
            }
        });

        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CommonUtils.judgeNetWork(SettingAct.this)){//如果当前网络不可用
                    return;
                }
                String feedbackStrTemp = feedbackEt.getText().toString();
                uploadFeedbackData(feedbackStrTemp);
            }
        });

        //返回
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 提交反馈的信息至服务器端
     * @param feedbackStrTemp
     */
    private void  uploadFeedbackData(String feedbackStrTemp){
        if (feedbackStrTemp.length() > 0) {
            WebProgress.createDialog(SettingAct.this);

            FeedBack feedBack = new FeedBack();
            feedBack.setPerson(BmobUser.getCurrentUser(Person.class));
            feedBack.setFeedbackContent(feedbackStrTemp);
            feedBack.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    WebProgress.webDismiss();
                    if (e == null) {
                        Toast.makeText(getApplication(), "提交成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), "提交失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
