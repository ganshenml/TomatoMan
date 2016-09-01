package com.example.ganshenml.tomatoman.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.tool.CommonUtils;
import com.example.ganshenml.tomatoman.tool.LogTool;
import com.example.ganshenml.tomatoman.tool.VerifyUtils;
import com.example.ganshenml.tomatoman.view.ClearEditTextView;
import com.example.ganshenml.tomatoman.view.WebProgress;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdatePassAct extends AppCompatActivity {
    private Toolbar updatePassTb;
    private ImageView backIv;
    private ClearEditTextView ctPasswordOld, ctPasswordNew, ctVerifyPasswordAgain;
    private Button updatePassBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pass);
        initViews();
        initDataViews();
        initListeners();
    }

    private void initViews() {
        updatePassTb = (Toolbar) findViewById(R.id.updatePassTb);
        updatePassTb.setTitle("");
        setSupportActionBar(updatePassTb);

        backIv = (ImageView) findViewById(R.id.backIv);
        ctPasswordOld = (ClearEditTextView) findViewById(R.id.ctPasswordOld);
        ctPasswordNew = (ClearEditTextView) findViewById(R.id.ctPasswordNew);
        ctVerifyPasswordAgain = (ClearEditTextView) findViewById(R.id.ctVerifyPasswordAgain);
        updatePassBtn = (Button) findViewById(R.id.updatePassBtn);
    }

    private void initDataViews() {


    }

    private void initListeners() {

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updatePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtils.judgeNetWork(UpdatePassAct.this)) {
                    return;
                }

                String passOld = ctPasswordOld.getText().toString();
                String passNew = ctPasswordNew.getText().toString();
                String passVerify = ctVerifyPasswordAgain.getText().toString();
                String verifyResults = VerifyUtils.verifyPassword(passNew);
                if (verifyResults.length() > 0) {
                    Toast.makeText(UpdatePassAct.this, "密码" + verifyResults, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if (!passNew.equals(passVerify)) {
                        Toast.makeText(UpdatePassAct.this, "两次新密码不一致", Toast.LENGTH_LONG).show();
                    } else {
                        WebProgress.createDialog(UpdatePassAct.this);
                        WebProgress.setLoadingStr("正在设置中...");
                        BmobUser.updateCurrentUserPassword(passOld, passNew, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                WebProgress.webDismiss();
                                if (e == null) {
                                    Toast.makeText(UpdatePassAct.this, "密码修改成功", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(UpdatePassAct.this, "密码修改失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    LogTool.log(LogTool.Aaron, "修改密码失败：" + e.getMessage().toString());
                                }
                            }
                        });


                    }
                }

            }
        });
    }

}
