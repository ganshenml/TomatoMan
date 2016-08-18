package com.example.ganshenml.tomatoman.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.text.BoringLayout;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.TomatoRecord;
import com.example.ganshenml.tomatoman.bean.beant.TomatoRecordT;
import com.example.ganshenml.tomatoman.bean.data.StaticData;
import com.example.ganshenml.tomatoman.callback.HttpCallback;
import com.example.ganshenml.tomatoman.tool.CommonUtils;
import com.example.ganshenml.tomatoman.tool.ConstantCode;
import com.example.ganshenml.tomatoman.tool.DbTool;
import com.example.ganshenml.tomatoman.tool.ImageViewUtils;
import com.example.ganshenml.tomatoman.tool.LogTool;
import com.example.ganshenml.tomatoman.tool.ShowDialogUtils;
import com.example.ganshenml.tomatoman.tool.SpTool;
import com.example.ganshenml.tomatoman.tool.ViewUtils;

import org.w3c.dom.Text;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 番茄计时完成的页面，进行各项数据的统计
 */
public class TomatoCompleteAct extends BaseActivity {
    private static final String TAG = "TomaotCompleteAct";
    private Button btnTomatoComplete;
    private static final int RESULTCODE_TO_MAIN = 101;
    private Toolbar tbToolbar_public;//公用toolbar
    private TextView leftTv, tvTitle_public;//公用toolbar标题
    private TextView tomatoTimeTv, taskTimeTv, tomatoNumTv, efficientTimeTv, tomatoNoteTv;
    private RatingBar evaluateLeverRb;
    private LinearLayout tomatoCompleteLl, toolBarLeftLl, stampLl;
    private FrameLayout tomatoCompleteFl;
    private ImageView rightIv, completeStateIv, hintLogoIv;

    private int tomatoCompleteTime, tomatoCompleteNum, tomatoEfficiencyTime;//完成的总番茄时间，番茄数量，番茄高效时间；

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

        tomatoCompleteLl = (LinearLayout) findViewById(R.id.tomatoCompleteLl);
        stampLl = (LinearLayout) findViewById(R.id.stampLl);
        tomatoCompleteFl = (FrameLayout) findViewById(R.id.tomatoCompleteFl);
        toolBarLeftLl = (LinearLayout) findViewById(R.id.toolBarLeftLl);
        leftTv = (TextView) findViewById(R.id.leftTv);
        tbToolbar_public = (Toolbar) findViewById(R.id.tbToolbar_public);
        tvTitle_public = (TextView) findViewById(R.id.tvTitle_public);
        rightIv = (ImageView) findViewById(R.id.rightIv);
        btnTomatoComplete = (Button) findViewById(R.id.btnTomatoComplete);
        setSupportActionBar(tbToolbar_public);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示默认的标题

        ViewUtils.setToolbar(TomatoCompleteAct.this, tbToolbar_public, tvTitle_public);//使用自定义的工具类方法设置toolbar的样式

        evaluateLeverRb = (RatingBar) findViewById(R.id.evaluateLeverRb);
        tomatoTimeTv = (TextView) findViewById(R.id.tomatoTimeTv);
        taskTimeTv = (TextView) findViewById(R.id.taskTimeTv);
        tomatoNumTv = (TextView) findViewById(R.id.tomatoNumTv);
        efficientTimeTv = (TextView) findViewById(R.id.efficientTimeTv);
        tomatoNoteTv = (TextView) findViewById(R.id.tomatoNoteTv);
        completeStateIv = (ImageView) findViewById(R.id.completeStateIv);
        hintLogoIv = (ImageView) findViewById(R.id.hintLogoIv);
        toolBarLeftLl.setVisibility(View.VISIBLE);
    }


    private void initListerners() {
        rightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "点击了右上角按钮", Toast.LENGTH_LONG).show();
            }
        });

        toolBarLeftLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getIntent().getFlags() == ConstantCode.ACTIVITY_FROM_MYTOMATO_CODE) {//如果是从MyTomatoAct界面跳转过来，则回退时直接退出
                    finish();
                    return;
                }

                ShowDialogUtils.showSimpleHintDialog(TomatoCompleteAct.this, "确定不保存当前数据吗？", new HttpCallback() {
                    @Override
                    public void onComplete(Object data) {
                        backToMainPage();
                    }
                });
                return;
            }
        });

        //点击按钮“完成”，保存数据至本地和服务器
        btnTomatoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SpTool.getInt(StaticData.SPTOMATOCOMPLETENUM, 0) == 0) {
                    Toast.makeText(TomatoCompleteAct.this, "一个番茄时间都木有完成，服务器大大是不会帮你保存的", Toast.LENGTH_LONG).show();
                } else {
                    TomatoRecord tomatoRecordTemp = saveTomatoRecordToLocal(); //1.保存该条记录至本地
//                    CommonUtils.resetSpData(); //2.重置相关sp数据（番茄数，番茄时间）——>在首页点击的时候重置即可
                    upLoadTomatoRecordToServer(tomatoRecordTemp);//3.上传至服务器该条数据并更新该条记录对应的本地数据（CreatedAt字段）
                }
                backToMainPage();//4.返回首页
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToMainPage();
    }

    private void initDataViews() {

        if (getIntent().getFlags() == ConstantCode.ACTIVITY_FROM_MYTOMATO_CODE) {//如果是从MyTomatoAct界面跳转过来，则获取数据进行显示

            Bundle bundle = getIntent().getExtras();
            TomatoRecordT tomatoRecordTTemp = (TomatoRecordT) bundle.getSerializable("tomatoRecordT");

            tomatoCompleteLl.setBackgroundResource(R.drawable.tomato_complete_background);
            stampLl.setBackgroundResource(R.drawable.tomato_completed_layout_custom);
            completeStateIv.setImageResource(R.mipmap.completed);

            Bitmap bitmapTemp = BitmapFactory.decodeResource(getResources(), R.drawable.hint_logo_completed);
            hintLogoIv.setImageBitmap(ImageViewUtils.getRoundedCornerBitmap(bitmapTemp, 150));

            tomatoNumTv.setText("本次收获了" + tomatoRecordTTemp.getTomatoNum().toString() + "个番茄");

            String htmlString1 = "<font>努力耕耘了 </font><font color=\"#52A5FF\">" + tomatoRecordTTemp.getTomatoTime() + "</font><font> 分钟</font>";
            tomatoTimeTv.setText(Html.fromHtml(htmlString1));

            taskTimeTv.setText("-------  " + tomatoRecordTTemp.getTaskTime() + "  -------");

            String htmlString2 = "<font>变身超人时间共 </font><font color=\"#52A5FF\">" + tomatoRecordTTemp.getEfficientTime() + "</font><font> 分钟</font>";
            efficientTimeTv.setText(Html.fromHtml(htmlString2));

            tomatoNoteTv.setText(tomatoRecordTTemp.getTomatoNote());
            evaluateLeverRb.setRating(tomatoRecordTTemp.getEvaluateLever());
            evaluateLeverRb.setIsIndicator(true);
            btnTomatoComplete.setVisibility(View.GONE);

        } else {

            leftTv.setText("首页");
            Bitmap bitmapTemp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_person);
            taskTimeTv.setText("-------  " + CommonUtils.getCurrentDataAndTime() + "  -------");
            hintLogoIv.setImageBitmap(ImageViewUtils.getRoundedCornerBitmap(bitmapTemp, 150));

            //一个番茄时间的判断样式
            if (SpTool.getInt(StaticData.SPTOMATOCOMPLETENUM, 0) > 0) {
                //显示完成的番茄工作时间和番茄数量
                tomatoCompleteLl.setBackgroundResource(R.drawable.tomato_complete_background);
                stampLl.setBackgroundResource(R.drawable.tomato_completed_layout_custom);
                completeStateIv.setImageResource(R.mipmap.completed);

                Bitmap bitmapTemp2 = BitmapFactory.decodeResource(getResources(), R.drawable.hint_logo_completed);
                hintLogoIv.setImageBitmap(ImageViewUtils.getRoundedCornerBitmap(bitmapTemp2, 150));

                int tomatoNumTemp =SpTool.getInt(StaticData.SPTOMATOCOMPLETENUM,0);
                tomatoNumTv.setText("本次收获了" + tomatoNumTemp + "个番茄");


                int tomatoTotalTimeTemp = SpTool.getInt(StaticData.SPWORKTIME,25) * tomatoNumTemp;
                LogTool.log(LogTool.Aaron,"TomatoCompleteAct initDataViews  sbWorkTime的值是： "+tomatoTotalTimeTemp);

                String htmlString1 = "<font>努力耕耘了 </font><font color=\"#52A5FF\">" + tomatoTotalTimeTemp+ "</font><font> 分钟</font>";
                tomatoTimeTv.setText(Html.fromHtml(htmlString1));

                taskTimeTv.setText("-------  " + CommonUtils.getCurrentDataAndTime() + "  -------");

                int tomatoEfficiencyTimeTemp  = SpTool.getInt(StaticData.SPTOMATOCOMPLETEEFFICIENTTIME,0);
                String htmlString2 = "<font>变身超人时间共 </font><font color=\"#52A5FF\">" + tomatoEfficiencyTimeTemp + "</font><font> 分钟</font>";
                efficientTimeTv.setText(Html.fromHtml(htmlString2));

            }

        }

        //印章动画效果+手机振动效果
        ScaleAnimation scaleAnimation = new ScaleAnimation(3.0f, 1.0f, 3.0f, 1.0f,
                getWindowManager().getDefaultDisplay().getWidth() / 2, getWindowManager().getDefaultDisplay().getHeight() / 2);
        scaleAnimation.setDuration(1000);
        completeStateIv.startAnimation(scaleAnimation);

        CommonUtils.startStampVibrator(TomatoCompleteAct.this);

        tomatoNumTv.setTextColor(Color.parseColor("#52A5FF"));

    }

    /**
     * 返回首页：1.完成数据保存；2.退出当前Activity；3.回到首页；
     */
    private void backToMainPage() {
        setResult(RESULTCODE_TO_MAIN);
        //1.完成数据保存

        //2.退出当前Activity
        finish();

        //3.回到首页
        Intent intent = new Intent(TomatoCompleteAct.this, MainActivity.class);
        startActivity(intent);

    }


    /**
     * 保存该条记录至本地
     */
    private TomatoRecord saveTomatoRecordToLocal() {
        TomatoRecord tomatoRecord = CommonUtils.constructUploadObject(tomatoNoteTv.getText().toString(), (int) evaluateLeverRb.getRating());
        TomatoRecordT tomatoRecordT = new TomatoRecordT(tomatoRecord);
        tomatoRecordT.save();
        return tomatoRecord;
    }

    /**
     * 上传至服务器该条数据并更新该条记录对应的本地数据（CreatedAt字段）
     */
    private void upLoadTomatoRecordToServer(final TomatoRecord tomatoRecord) {
        tomatoRecord.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    DbTool.update_CreatedAt_InLocal(tomatoRecord);
                } else {
                    LogTool.log(LogTool.Aaron, TAG + " upLoadTomatoRecordToServer 异常： " + e.toString());
                }
            }
        });
    }

}
