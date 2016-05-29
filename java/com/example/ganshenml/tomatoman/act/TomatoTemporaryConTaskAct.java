package com.example.ganshenml.tomatoman.act;
/**
 * 休息时间到点后跳转的临时页面：提供终止任务和继续任务入口
 */
import android.content.Intent;
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
import com.example.ganshenml.tomatoman.view.ContinueTaskBigCircleView;
import com.example.ganshenml.tomatoman.view.StartRestCircleView;

public class TomatoTemporaryConTaskAct extends AppCompatActivity {
    private CompleteTaskCircleView completeTaskCircleViewId;
    private ContinueTaskBigCircleView continueTaskBigCircleViewId;
    private Toolbar tbToolbar_public;
    private TextView tvTitle_public;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_temporary_con_task);

        initViews();
        litenerMethod();
    }

    //对回退事件做弹窗处理
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果点击了回退：弹窗
            ShowDialogUtils.showDialog(TomatoTemporaryConTaskAct.this, TomatoCompleteAct.class, this);//调用自定义工具类方法显示弹窗
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    //------------------------------------------------------------------以下为自定义方法----------------------------------------------------
    private void litenerMethod() {
        //点击进入“开始番茄计时的页面”
        continueTaskBigCircleViewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.结束当前activity
                finish();

                //2.发送“番茄工作”的通知
                Intent intent = new Intent(TomatoTemporaryConTaskAct.this, TomatoCountTimeAct.class);
                NotificationUtls.sendNotification(TomatoTemporaryConTaskAct.this, ConstantCode.HOMEFRAGMETN_REQUEST_CODE, intent, R.layout.notification_layout);//调用自定义工具类方法发送notification

                //3.进入新的activity
                startActivity(intent);

            }
        });

        //点击进入“番茄数据统计页”
        completeTaskCircleViewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TomatoTemporaryConTaskAct.this, TomatoCompleteAct.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initViews() {
        continueTaskBigCircleViewId = (ContinueTaskBigCircleView) findViewById(R.id.continueTaskBigCircleViewId);
        completeTaskCircleViewId = (CompleteTaskCircleView) findViewById(R.id.completeTaskCircleViewId);

        //设置toolbar样式
        tbToolbar_public = (Toolbar) findViewById(R.id.tbToolbar_public);
        tvTitle_public = (TextView) findViewById(R.id.tvTitle_public);
        setSupportActionBar(tbToolbar_public);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示默认的标题

        ViewUtils.setToolbar(TomatoTemporaryConTaskAct.this,tbToolbar_public,tvTitle_public);//使用自定义的工具类方法设置toolbar的样式
    }


}
