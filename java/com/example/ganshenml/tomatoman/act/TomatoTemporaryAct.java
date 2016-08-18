package com.example.ganshenml.tomatoman.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.data.StaticData;
import com.example.ganshenml.tomatoman.tool.ConstantCode;
import com.example.ganshenml.tomatoman.tool.NotificationUtls;
import com.example.ganshenml.tomatoman.tool.ShowDialogUtils;
import com.example.ganshenml.tomatoman.tool.SpTool;
import com.example.ganshenml.tomatoman.tool.ViewUtils;
import com.example.ganshenml.tomatoman.view.CompleteTaskCircleView;
import com.example.ganshenml.tomatoman.view.StartRestCircleView;

/*
作为完成一个番茄事件后的临时状态：可以选择“开始休息”、“完成任务”、“进入高效时间领域”
 */
public class TomatoTemporaryAct extends BaseActivity {
    private Button startRestBtn,completeTaskBtn,efficiencyZoneBtn;
    private Toolbar tbToolbar_public;
    private TextView tvTitle_public;
    private LinearLayout obtainedTomatoLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_temporary);

        initViews();
        listenerMethod();
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
    private void listenerMethod() {

        showObtainedTomatoViews();//显示已经获得的番茄样式（如果有）

        //点击:1.进入“休息的计时页面”;2.发送休息的通知
        startRestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TomatoTemporaryAct.this, TomatoRestAct.class);
                NotificationUtls.sendNotification(TomatoTemporaryAct.this, ConstantCode.TOMATOTEMPORARYACT_REST_REQUEST_CODE, intent, R.layout.notification_rest_layout);//调用自定义工具类方法发送notification
                finish();
                startActivity(intent);
            }
        });

        //点击进入“番茄数据统计页”
        completeTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TomatoTemporaryAct.this, TomatoCompleteAct.class);
                finish();
                startActivity(intent);
            }
        });

        //点击：1.进入“高效时间领域”页；2.发送进入该“高效时间”领域的通知
        efficiencyZoneBtn.setOnClickListener(new View.OnClickListener() {
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
        obtainedTomatoLl = (LinearLayout)findViewById(R.id.obtainedTomatoLl);
        startRestBtn = (Button) findViewById(R.id.startRestBtn);
        completeTaskBtn = (Button) findViewById(R.id.completeTaskBtn);
        efficiencyZoneBtn = (Button) findViewById(R.id.efficiencyZoneBtn);

        tbToolbar_public = (Toolbar) findViewById(R.id.tbToolbar_public);
        tvTitle_public = (TextView) findViewById(R.id.tvTitle_public);
        setSupportActionBar(tbToolbar_public);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示默认的标题

        ViewUtils.setToolbar(TomatoTemporaryAct.this,tbToolbar_public,tvTitle_public);//使用自定义的工具类方法设置toolbar的样式

    }

    /**
     * 显示已经获得的番茄样式（如果有）
     */
    private void showObtainedTomatoViews(){
        int obtainedTomatoNum = SpTool.getInt(StaticData.SPTOMATOCOMPLETENUM,0);
        if(obtainedTomatoNum>0){
            obtainedTomatoLl.setVisibility(View.VISIBLE);
            for (int i = 0; i < obtainedTomatoNum; i++) {
                ImageView imageView = new ImageView(TomatoTemporaryAct.this);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(90,90);
                imageView.setLayoutParams(layoutParams);
                imageView.setPadding(10,10,10,10);
                imageView.setImageResource(R.mipmap.tomato_red);
                obtainedTomatoLl.addView(imageView);
            }

            //透明度变化的动画
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1.0f);
            alphaAnimation.setDuration(3000);
            alphaAnimation.setRepeatCount(Animation.INFINITE);
            obtainedTomatoLl.startAnimation(alphaAnimation);
        }

    }

}
