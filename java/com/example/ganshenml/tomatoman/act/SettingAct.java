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
import com.example.ganshenml.tomatoman.bean.Extra;
import com.example.ganshenml.tomatoman.bean.FeedBack;
import com.example.ganshenml.tomatoman.bean.Person;
import com.example.ganshenml.tomatoman.bean.beant.ExtraT;
import com.example.ganshenml.tomatoman.callback.HttpCallback;
import com.example.ganshenml.tomatoman.util.CommonUtils;
import com.example.ganshenml.tomatoman.util.DbTool;
import com.example.ganshenml.tomatoman.util.LogTool;
import com.example.ganshenml.tomatoman.util.ShowDialogUtils;
import com.example.ganshenml.tomatoman.util.ToActivityPage;

import org.litepal.LitePalApplication;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SettingAct extends BaseActivity {
    private LinearLayout logoutLl;
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
        logoutLl = (LinearLayout) findViewById(R.id.logoutLl);
        feedbackEt = (EditText) findViewById(R.id.feedbackEt);
        feedbackBtn = (Button) findViewById(R.id.feedbackBtn);
        backIv = (ImageView) findViewById(R.id.backIv);
    }

    private void initDataViews() {
        LogTool.log(LogTool.Aaron, " settingFragment initDataViews 进入了");
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

    }

    private void initListeners() {

        logoutLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注销账号弹窗
                ShowDialogUtils.showSimpleHintDialog(SettingAct.this, "确定退出该账号？", new HttpCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        BmobUser.logOut();
                        ToActivityPage.turnToSimpleAct(SettingAct.this, LoginAct.class);
                        finish();
                    }
                });
            }
        });

        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogTool.log(LogTool.Aaron, " settingFragment 点击了提交反馈按钮");
                String feedbackStrTemp = feedbackEt.getText().toString();
                if (feedbackStrTemp.length() > 0) {
                    FeedBack feedBack = new FeedBack();
                    feedBack.setPerson(BmobUser.getCurrentUser(Person.class));
                    feedBack.setFeedbackContent(feedbackStrTemp);
                    feedBack.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(getApplication(), "提交成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplication(), "提交失败", Toast.LENGTH_SHORT).show();
                                LogTool.log(LogTool.Aaron, " settingFragment 提交反馈失败： " + e.toString());
                            }
                        }
                    });
                }
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


}
