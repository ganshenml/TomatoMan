package com.example.ganshenml.tomatoman.act;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.service.CountTimeNumService;
import com.example.ganshenml.tomatoman.util.ConstantCode;
import com.example.ganshenml.tomatoman.util.ContextManager;
import com.example.ganshenml.tomatoman.util.NotificationUtls;
import com.example.ganshenml.tomatoman.util.ShowDialogUtils;
import com.example.ganshenml.tomatoman.util.ViewUtils;
import com.example.ganshenml.tomatoman.view.TomatoCountSurfaceView;

/**
 * 用来展示“高效时间领域”的Activity
 */
public class TomatoEfficiencyAct extends BaseActivity {

    TomatoCountSurfaceView tomatoCountSurfaceView;
    Intent myIntent;
    private CountTimeNumService countTimeNumService;
    CountTimeNumService.MyBinder myBinder;
    ServiceConnection serviceConnection;
    int countTimeNum = 0;
    private Toolbar tbToolbar_public;
    private TextView tvTitle_public;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int countTimeGoal = 0;//目标计时时间,从sharedPreference中获取

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_efficiency);

        initViews();

        //初始化计时的数据(从sharedPreference中获取)
        initData();

        //通过Binder的方式来获得CountTimeNumService的对象(myBinder)
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (CountTimeNumService.MyBinder) service;
                //myBinder.getService().setCountTimeGoal(countTimeGoal * 60);//将从sharedPreference中获取的设定时间传给service
                myBinder.getService().setCountTimeGoal((24 * 60 * 60));//高效领域可以先将时间设定为24小时
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };

        //指定service在后台继续计算时间
        myIntent = new Intent(TomatoEfficiencyAct.this, CountTimeNumService.class);
        myIntent.putExtra("countTimeGoal", countTimeGoal);
        bindService(myIntent, serviceConnection, BIND_AUTO_CREATE);

        //将该activity加入至ContextManager的List中进行管理
        ContextManager.addContext(this);
    }


    //按钮点击：1.停止计时；2.退出当前Activity（隐含退出了服务）；3.跳转至“一个番茄完成后的中间页”（并传递数据）
    public void stop(View view) {
        //1.停止计时
        tomatoCountSurfaceView.countThread.isStop = true;//别忘了以后这里要做线程的回收之类的工作

        //2.退出当前Activity
        finish();

        //3.跳转至“番茄计时完成页”
        Intent intent = new Intent(TomatoEfficiencyAct.this, TomatoTemporaryAct.class);
        //传递数据。。。。。。。。。。。。。。。。。。。。。。。。
        startActivity(intent);
    }

    //通过onRestart方法获取后台Service计算的时间值，并传递给自定义的SurfaceView（这里是TestView）
    @Override
    protected void onRestart() {
        super.onRestart();
        countTimeNum = (int) myBinder.getService().getCountTimeNum();
        tomatoCountSurfaceView.setEndAngle(countTimeNum);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tomatoCountSurfaceView.countThread != null) {//赋值线程为null，等待gc回收
            tomatoCountSurfaceView.countThread = null;
        }

        //停止Service
        stopMyService();

        //取消掉通知显示
        NotificationUtls.cancelNotification(this, ConstantCode.TOMATOTEMPORARYACT_EFFICIENCE_REQUEST_CODE);

        //将该activity从ContextManager的List中进行移除
        ContextManager.removeContext(this);
    }


    //对回退事件处理：回退给出弹窗——>是否终止任务（是：进入“番茄计时完成页“）。
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果点击了回退
            ShowDialogUtils.showDialog(TomatoEfficiencyAct.this, TomatoCompleteAct.class, this);//调用自定义工具类方法显示弹窗
            return false;//不允许通过回退键的方式退出该Activity
        }
        return super.onKeyDown(keyCode, event);
    }


    //------------------------------------------------------------以下为自定义方法------------------------------------------

    private void initViews() {
        tbToolbar_public = (Toolbar) findViewById(R.id.tbToolbar_public);
        tvTitle_public = (TextView) findViewById(R.id.tvTitle_public);
        setSupportActionBar(tbToolbar_public);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示默认的标题

        ViewUtils.setToolbar(TomatoEfficiencyAct.this, tbToolbar_public, tvTitle_public);//使用自定义的工具类方法设置toolbar的样式

        tomatoCountSurfaceView = (TomatoCountSurfaceView) findViewById(R.id.efficiencyCircleViewId);
    }


    private void initData() {
        sharedPreferences = getSharedPreferences("TomaotSetting", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        countTimeGoal = sharedPreferences.getInt("longRestTime", 20);//如果是还未创建sharedPreference，则默认值为25

        //为SurfaceView设置每秒画多少度
//        tomatoCountSurfaceView.setDivisionNum((float) (360 / (countTimeGoal * 60)));
        tomatoCountSurfaceView.setDivisionNum((float) (360 / (0.1 * 60)));//测试期间先默认为0.1分钟

        //实例化binder对象
        countTimeNumService = new CountTimeNumService();
//        countTimeNumService.setCountTimeGoal(countTimeGoal * 60);
        countTimeNumService.setCountTimeGoal((24 * 60 * 60));//高效领域可以先把时间设定为24小时
    }

    //停止运行Service
    private void stopMyService() {
        if (serviceConnection != null) {
            myBinder.getService().setStopFlag(true);
            unbindService(serviceConnection);
            stopService(myIntent);
        }
    }
}
