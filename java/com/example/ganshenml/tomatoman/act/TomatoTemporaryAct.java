package com.example.ganshenml.tomatoman.act;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
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
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.data.StaticData;
import com.example.ganshenml.tomatoman.tool.CommonUtils;
import com.example.ganshenml.tomatoman.tool.ConstantCode;
import com.example.ganshenml.tomatoman.tool.LogTool;
import com.example.ganshenml.tomatoman.tool.NotificationUtls;
import com.example.ganshenml.tomatoman.tool.ShowDialogUtils;
import com.example.ganshenml.tomatoman.tool.SpTool;
import com.example.ganshenml.tomatoman.tool.ViewUtils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/*
作为完成一个番茄事件后的临时状态：可以选择“开始休息”、“完成任务”、“进入高效时间领域”
 */
public class TomatoTemporaryAct extends BaseActivity {
    private Button startRestBtn, completeTaskBtn, efficiencyZoneBtn;
    private Toolbar tbToolbar_public;
    private TextView tvTitle_public;
    private LinearLayout obtainedTomatoLl;
    private Vibrator vibrator;
    private MediaPlayer mMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_temporary);

        initViews();
        initDataViews();
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

                Intent intent1 = getIntent();
                if (intent1.getBooleanExtra("isFromTomatoRestAct", false)) {    //如果是从休息页面跳转过来，则页面的样式要变换
                    LogTool.log(LogTool.Aaron, "TomatoTemporary 从TomatoRestAct页面计时完成跳转而来，点击了——继续工作");
                    Intent intent = new Intent(TomatoTemporaryAct.this, TomatoCountTimeAct.class);
                    NotificationUtls.sendNotification(TomatoTemporaryAct.this, ConstantCode.HOMEFRAGMETN_REQUEST_CODE, intent, R.layout.notification_layout);//调用自定义工具类方法发送notification
                    finish();
                    startActivity(intent);
                } else {
                    LogTool.log(LogTool.Aaron, "TomatoTemporary 从TomatoCountTime页面计时完成跳转而来，点击了——开始休息");
                    Intent intent = new Intent(TomatoTemporaryAct.this, TomatoRestAct.class);
                    NotificationUtls.sendNotification(TomatoTemporaryAct.this, ConstantCode.TOMATOTEMPORARYACT_REST_REQUEST_CODE, intent, R.layout.notification_rest_layout);//调用自定义工具类方法发送notification
                    finish();
                    startActivity(intent);
                }
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
        obtainedTomatoLl = (LinearLayout) findViewById(R.id.obtainedTomatoLl);
        startRestBtn = (Button) findViewById(R.id.startRestBtn);
        completeTaskBtn = (Button) findViewById(R.id.completeTaskBtn);
        efficiencyZoneBtn = (Button) findViewById(R.id.efficiencyZoneBtn);

        tbToolbar_public = (Toolbar) findViewById(R.id.tbToolbar_public);
        tvTitle_public = (TextView) findViewById(R.id.tvTitle_public);
        setSupportActionBar(tbToolbar_public);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示默认的标题

        ViewUtils.setToolbar(TomatoTemporaryAct.this, tbToolbar_public, tvTitle_public);//使用自定义的工具类方法设置toolbar的样式

    }

    private void initDataViews() {
        NotificationUtls.cancelAllNotification(this);//取消所有通知


        //获取振动的参数:如果是振动，则调用振动工具类方法
        if (!getIntent().getBooleanExtra("isFromTomatoEfficiencyAct", false)) {//如果不是从高效页面跳转过来，则需要振动提醒和播放铃声
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (SpTool.getBoolean(StaticData.SPVIBRATEALARM, true)) {
                long[] patterns = new long[]{0, 1000, 2000, 1000, 2000, 1000};
                vibrator.vibrate(patterns, -1);//不重复，仅一次
            }

            if (SpTool.getBoolean(StaticData.SPRINGTONEALARM, false)) {//如果设置过铃声提醒，则进行播放铃声
                toPlayVideo();
            }

            //计时程序，播放4分钟后停止设备播放设置
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    releaseDeviceSetting();
                }
            },4*60*1000);
        }

        Intent intent = getIntent();
        if (intent.getBooleanExtra("isFromTomatoRestAct", false)) {    //如果是从休息页面跳转过来，则页面的样式要变换
            startRestBtn.setText("继续任务");
            startRestBtn.setTextColor(getResources().getColor(R.color.grass_blue));
            startRestBtn.setBackgroundResource(R.drawable.grass_blue_stroke_button_style);

            Drawable nav_up = getResources().getDrawable(R.mipmap.arrow_grass_blue);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            startRestBtn.setCompoundDrawables(null, null, nav_up, null);
        }
    }

    @Override
    protected void onDestroy() {
        releaseDeviceSetting();//释放振动、响铃等资源设置
        super.onDestroy();
    }

    /**
     * 显示已经获得的番茄样式（如果有）
     */
    private void showObtainedTomatoViews() {
        int obtainedTomatoNum = SpTool.getInt(StaticData.SPTOMATOCOMPLETENUM, 0);
        if (obtainedTomatoNum > 0) {
            obtainedTomatoLl.setVisibility(View.VISIBLE);
            for (int i = 0; i < obtainedTomatoNum; i++) {
                ImageView imageView = new ImageView(TomatoTemporaryAct.this);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(90, 90);
                imageView.setLayoutParams(layoutParams);
                imageView.setPadding(10, 10, 10, 10);
                imageView.setImageResource(R.mipmap.tomato_red);
                obtainedTomatoLl.addView(imageView);
            }

            //透明度变化的动画
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(3000);
            alphaAnimation.setRepeatCount(Animation.INFINITE);
            obtainedTomatoLl.startAnimation(alphaAnimation);
        }

    }

    //播放铃声
    private void toPlayVideo() {
        String myUriStr = SpTool.getString(StaticData.SPRINGTONEALARMURI, "");
        if (!myUriStr.equals("")) {//已经设置过音频
            mMediaPlayer = MediaPlayer.create(this, Uri.parse(myUriStr));

            if (mMediaPlayer == null) {//如果未自定义铃声，则调用系统默认的铃声
                Toast.makeText(this, "该铃声不存在，请重新选择", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            mMediaPlayer = MediaPlayer.create(this, CommonUtils.getSystemDefaultRingtoneUri(this));
        }
        mMediaPlayer.setLooping(true);

        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer.start();
            }
        });
    }

    /**
     * 释放振动、响铃等资源设置
     */
    private void releaseDeviceSetting() {
        //释放vibrator（用户点击按钮进入下一次，若此时仍然引用vibrator，则该activity此时未被完全释放会影响程序逻辑）
        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }

        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
        }
    }

}
