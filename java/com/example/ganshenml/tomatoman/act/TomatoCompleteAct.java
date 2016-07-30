package com.example.ganshenml.tomatoman.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.beant.TomatoRecordT;
import com.example.ganshenml.tomatoman.callback.HttpCallback;
import com.example.ganshenml.tomatoman.util.ConstantCode;
import com.example.ganshenml.tomatoman.util.ShowDialogUtils;
import com.example.ganshenml.tomatoman.util.ViewUtils;

/**
 * 番茄计时完成的页面，进行各项数据的统计
 */
public class TomatoCompleteAct extends BaseActivity {
    private static final String TAG = "TomaotCompleteAct";
    private Button btnTomatoComplete;
    private static final int RESULTCODE_TO_MAIN = 101;
    private Toolbar tbToolbar_public;//公用toolbar
    private TextView tvTitle_public;//公用toolbar标题
    private TextView completeStateTv, taskTimeTv, tomatoNumTv, efficientTimeTv, tomatoNoteTv;
    private RatingBar evaluateLeverRb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_complete);

        initViews();
        initDataViews();
        initListerners();
    }

    //对回退事件做弹窗处理
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果点击了回退
            if (getIntent().getFlags() == ConstantCode.ACTIVITY_FROM_MYTOMATO_CODE) {//如果是从MyTomatoAct界面跳转过来，则回退时直接退出
                finish();
                return true;
            }

            ShowDialogUtils.showSimpleHintDialog(TomatoCompleteAct.this, "确定不保存当前数据吗？", new HttpCallback() {
                @Override
                public void onComplete(Object data) {
                    Intent intent = new Intent(TomatoCompleteAct.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //-------------------------------------------------------以下为自定义方法-------------------------------------------
    private void initViews() {

        tbToolbar_public = (Toolbar) findViewById(R.id.tbToolbar_public);
        tvTitle_public = (TextView) findViewById(R.id.tvTitle_public);
        setSupportActionBar(tbToolbar_public);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示默认的标题

        ViewUtils.setToolbar(TomatoCompleteAct.this, tbToolbar_public, tvTitle_public);//使用自定义的工具类方法设置toolbar的样式

        evaluateLeverRb = (RatingBar) findViewById(R.id.evaluateLeverRb);
        completeStateTv = (TextView) findViewById(R.id.completeStateTv);
        taskTimeTv = (TextView) findViewById(R.id.taskTimeTv);
        tomatoNumTv = (TextView) findViewById(R.id.tomatoNumTv);
        efficientTimeTv = (TextView) findViewById(R.id.efficientTimeTv);
        tomatoNoteTv = (TextView) findViewById(R.id.tomatoNoteTv);

        btnTomatoComplete = (Button) findViewById(R.id.btnTomatoComplete);


    }


    private void initListerners() {

        //点击“完成”按钮：1.完成数据保存；2.退出当前Activity；3.回到首页；
        btnTomatoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULTCODE_TO_MAIN);
                //1.完成数据保存

                //2.退出当前Activity
                finish();

                //3.回到首页
                Intent intent = new Intent(TomatoCompleteAct.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //点击“完成并分享”
    }

    private void initDataViews() {

        if (getIntent().getFlags() == ConstantCode.ACTIVITY_FROM_MYTOMATO_CODE) {//如果是从MyTomatoAct界面跳转过来，则获取数据进行显示

            Bundle bundle = getIntent().getExtras();
            TomatoRecordT tomatoRecordTTemp = (TomatoRecordT) bundle.getSerializable("tomatoRecordT");

            completeStateTv.setText(tomatoRecordTTemp.getCompleteState());
            taskTimeTv.setText(tomatoRecordTTemp.getTaskTime());
            tomatoNumTv.setText(tomatoRecordTTemp.getTomatoNum().toString());
            efficientTimeTv.setText(tomatoRecordTTemp.getEfficientTime());
            tomatoNoteTv.setText(tomatoRecordTTemp.getTomatoNote());
            evaluateLeverRb.setRating(tomatoRecordTTemp.getEvaluateLever());
            evaluateLeverRb.setIsIndicator(true);

            return;
        }

    }
}
