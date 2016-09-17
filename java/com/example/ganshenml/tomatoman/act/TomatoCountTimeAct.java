package com.example.ganshenml.tomatoman.act;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.data.StaticData;
import com.example.ganshenml.tomatoman.service.CountTimeNumService;
import com.example.ganshenml.tomatoman.tool.ConstantCode;
import com.example.ganshenml.tomatoman.tool.ContextManager;
import com.example.ganshenml.tomatoman.tool.LogTool;
import com.example.ganshenml.tomatoman.tool.NotificationUtls;
import com.example.ganshenml.tomatoman.tool.ShowDialogUtils;
import com.example.ganshenml.tomatoman.tool.SpTool;
import com.example.ganshenml.tomatoman.tool.ViewUtils;
import com.example.ganshenml.tomatoman.view.TomatoCountSurfaceView;

public class TomatoCountTimeAct extends BaseActivity {
    private TomatoCountSurfaceView tomatoCountSurfaceView;
    private Intent myIntent;
    private CountTimeNumService countTimeNumService;
    private CountTimeNumService.MyBinder myBinder;
    private ServiceConnection serviceConnection;
    private int countTimeNum = 0;
    private Toolbar tbToolbar_public;
    private TextView tvTitle_public;
    private int countTimeGoal = 0;//目标计时时间,从sharedPreference中获取

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.log(LogTool.Aaron, " TomatoCountTimeAct onCreate方法执行了");
        setContentView(R.layout.activity_tomato_count_time);

        initViews();

        //初始化计时的数据(从sharedPreference中获取)
        initData();

        if (serviceConnection != null) {
            serviceConnection = null;
        }
        //通过Binder的方式来获得CountTimeNumService的对象(myBinder)
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (CountTimeNumService.MyBinder) service;
//                myBinder.getService().setCountTimeGoal(countTimeGoal * 60);//将从sharedPreference中获取的设定时间传给service
//                myBinder.getService().setCountTimeGoal((int) (1.0));//先将数据设定为0.1分钟方便测试
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                LogTool.log(LogTool.Aaron," TomatoCountTimeAct Service 异常停止");
            }

        };

        //指定service在后台继续计算时间
        myIntent = new Intent(TomatoCountTimeAct.this, CountTimeNumService.class);
        myIntent.putExtra("countTimeGoal", countTimeGoal);
        bindService(myIntent, serviceConnection, BIND_AUTO_CREATE);
        LogTool.log(LogTool.Aaron," TomatoCountTimeAct bindService 执行了");

        //将该activity加入至ContextManager的List中进行管理
        ContextManager.addContext(this);

    }


    //按钮点击：1.停止计时；2.退出当前Activity（隐含退出了服务）；3.跳转至“番茄计时完成页”（并传递数据）
    public void stop(View view) {
        //1.停止计时
        tomatoCountSurfaceView.countThread.isStop = true;//别忘了以后这里要做线程的回收之类的工作

        //2.退出当前Activity
        finish();

        //3.跳转至“番茄计时完成页”
        Intent intent = new Intent(TomatoCountTimeAct.this, TomatoCompleteAct.class);
        //传递数据。。。。。。。。。。。。。。。。。。。。。。。。

        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
       tomatoCountSurfaceView.getCountThread().setPause(true);
    }

    //通过onRestart方法获取后台Service计算的时间值，并传递给自定义的SurfaceView（这里是TestView）
    @Override
    protected void onRestart() {
        super.onRestart();
        LogTool.log(LogTool.Aaron," TomatoCountTimeAct onRestart执行了");
        countTimeNum = (int) myBinder.getService().getCountTimeNum();
        tomatoCountSurfaceView.setEndAngle(countTimeNum);
        tomatoCountSurfaceView.getCountThread().setPause(false);
    }


    @Override
    protected void onDestroy() {
        LogTool.log(LogTool.Aaron,"TomatoCountTimeAct onDestroy 执行了");

        //停止Service
        stopMyService();

        if (tomatoCountSurfaceView.countThread != null) {//赋值线程为null，等待gc回收
            tomatoCountSurfaceView.countThread.isStop = true;
            tomatoCountSurfaceView.countThread.interrupt();
            tomatoCountSurfaceView.countThread = null;
        }

        //取消掉notification的显示
        NotificationUtls.cancelNotification(this, ConstantCode.HOMEFRAGMETN_REQUEST_CODE);

        //将该activity从ContextManager的List中进行移除
        ContextManager.removeContext(this);
        super.onDestroy();
    }


    //对回退事件处理：回退给出弹窗——>是否终止任务（是：进入“番茄计时完成页“）。
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果点击了回退
            ShowDialogUtils.showDialog(TomatoCountTimeAct.this, TomatoCompleteAct.class, this);//调用自定义工具类方法显示弹窗
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

        ViewUtils.setToolbar(TomatoCountTimeAct.this, tbToolbar_public, tvTitle_public);//使用自定义的工具类方法设置toolbar的样式

        tomatoCountSurfaceView = (TomatoCountSurfaceView) findViewById(R.id.tomatoSurfaceViewId);

    }

    private void initData() {
//        sharedPreferences = getSharedPreferences("TomaotSetting", MODE_PRIVATE);
//        editor = sharedPreferences.edit();
        countTimeGoal = SpTool.getInt(StaticData.SPWORKTIME, 25);//如果是还未创建sharedPreference，则默认值为25
        LogTool.log(LogTool.Aaron,"initData SPWORKTIME : "+countTimeGoal);

        //为SurfaceView设置每秒画多少度
        tomatoCountSurfaceView.setDivisionNum((float) (360 / (countTimeGoal * 60.0)));
//        tomatoCountSurfaceView.setDivisionNum((float) (360 / (1)));//测试期间先默认为0.1分钟

        //实例化binder对象
//        countTimeNumService = new CountTimeNumService();
//        countTimeNumService.setCountTimeGoal(countTimeGoal * 60);
//        countTimeNumService.setCountTimeGoal((int) (0.1 * 60));//测试期间先默认为0.1分钟

    }


    //停止运行Service
    private void stopMyService() {
        if (serviceConnection != null) {
            myBinder.getService().setStopFlag(true);
            myBinder.getService().setCountTimeNumThread(null);
            unbindService(serviceConnection);
            stopService(myIntent);
            LogTool.log(LogTool.Aaron,"TomatoCountTimeAct service 停止操作执行了");

        }
    }
}

