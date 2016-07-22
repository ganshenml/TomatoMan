package com.example.ganshenml.tomatoman.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.util.ConstantCode;
import com.example.ganshenml.tomatoman.util.NotificationUtls;
import com.example.ganshenml.tomatoman.util.ShowDialogUtils;
import com.example.ganshenml.tomatoman.util.ViewUtils;
import com.example.ganshenml.tomatoman.view.CompleteTaskCircleView;
import com.example.ganshenml.tomatoman.view.StartRestCircleView;

/*
作为完成一个番茄事件后的临时状态：可以选择“开始休息”、“完成任务”、“进入高效时间领域”
 */
public class TomatoTemporaryAct extends BaseActivity {
    private CompleteTaskCircleView completeTaskCircleViewId;
    private TextView tvEfficiencyZone;
    private StartRestCircleView startRestCircleViewId;
    private Toolbar tbToolbar_public;
    private TextView tvTitle_public;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_temporary);

        initViews();
        litenerMethod();
    }

    //对回退事件做弹窗处理
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果点击了回退：弹窗
            ShowDialogUtils.showDialog(TomatoTemporaryAct.this, TomatoCompleteAct.class, this);//调用自定义工具类方法显示弹窗
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    //------------------------------------------------------------------以下为自定义方法----------------------------------------------------
    private void litenerMethod() {
        //点击:1.进入“休息的计时页面”;2.发送休息的通知
        startRestCircleViewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TomatoTemporaryAct.this, TomatoRestAct.class);
                NotificationUtls.sendNotification(TomatoTemporaryAct.this, ConstantCode.TOMATOTEMPORARYACT_REST_REQUEST_CODE, intent, R.layout.notification_rest_layout);//调用自定义工具类方法发送notification
                finish();
                startActivity(intent);
            }
        });

        //点击进入“番茄数据统计页”
        completeTaskCircleViewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TomatoTemporaryAct.this, TomatoCompleteAct.class);
                finish();
                startActivity(intent);
            }
        });

        //点击：1.进入“高效时间领域”页；2.发送进入该“高效时间”领域的通知
        tvEfficiencyZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TomatoTemporaryAct.this, TomatoEfficiencyAct.class);
                NotificationUtls.sendNotification(TomatoTemporaryAct.this, ConstantCode.TOMATOTEMPORARYACT_EFFICIENCE_REQUEST_CODE, intent, R.layout.notification_efficiency_layout);//调用自定义工具类方法发送notification
                finish();
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        startRestCircleViewId = (StartRestCircleView) findViewById(R.id.startRestCircleViewId);
        completeTaskCircleViewId = (CompleteTaskCircleView) findViewById(R.id.completeTaskCircleViewId);
        tvEfficiencyZone = (TextView) findViewById(R.id.tvEfficiencyZone);

        tbToolbar_public = (Toolbar) findViewById(R.id.tbToolbar_public);
        tvTitle_public = (TextView) findViewById(R.id.tvTitle_public);
        setSupportActionBar(tbToolbar_public);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示默认的标题

        ViewUtils.setToolbar(TomatoTemporaryAct.this,tbToolbar_public,tvTitle_public);//使用自定义的工具类方法设置toolbar的样式

    }


}
