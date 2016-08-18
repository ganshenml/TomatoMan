package com.example.ganshenml.tomatoman.act;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.data.StaticData;
import com.example.ganshenml.tomatoman.tool.LogTool;
import com.example.ganshenml.tomatoman.tool.SpTool;

public class TomatoSettingAct extends BaseActivity {

    private TextView tvWorkTime, tvShortRest, tvLongRest;
    private AppCompatSeekBar sbWorkTime, sbShortRest, sbLongRest;
    private CheckBox cbVibrateAlarm, cbRingtoneAlarm;//振动提醒和响铃提醒的设置
//    private SharedPreferences sharedPreferences;
//    private SharedPreferences.Editor editor;
    private Toolbar tomatoSettingTb;
    private ImageView backIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_setting);

        initViews();
        initListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    //通过该周期方法来保存用户当前设置:1.写入本地文件；（不用传输至网络）
    @Override
    protected void onStop() {
        super.onStop();
//        editor.putInt("workTime", sbWorkTime.getProgress());
//        editor.putInt("shortRestTime", sbShortRest.getProgress());
//        editor.putInt("longRestTime", sbLongRest.getProgress());
//
//        editor.putBoolean("vibrateAlarm", cbVibrateAlarm.isChecked());
//        editor.putBoolean("ringtoneAlarm", cbRingtoneAlarm.isChecked());
//
//        editor.commit();
        LogTool.log(LogTool.Aaron,"TomatoSettingAct onStop sbWorkTime的值是： "+sbWorkTime.getProgress());
        SpTool.putInt(StaticData.SPWORKTIME, sbWorkTime.getProgress());
        SpTool.putInt(StaticData.SPSHORTRESTTIME, sbShortRest.getProgress());
        SpTool.putInt(StaticData.SPLONGRESTTIME, sbLongRest.getProgress());

        SpTool.putBoolean(StaticData.SPVIBRATEALARM, cbVibrateAlarm.isChecked());
        SpTool.putBoolean(StaticData.SPRINGTONEALARM, cbRingtoneAlarm.isChecked());

    }

    //-------------------------------------------------------------------以下为自定义方法-----------------------------------------------------
    //初始化控件
    private void initViews() {
        tomatoSettingTb = (Toolbar) findViewById(R.id.tomatoSettingTb);
        tomatoSettingTb.setTitle("");
        setSupportActionBar(tomatoSettingTb);

        backIv = (ImageView)findViewById(R.id.backIv);
        tvWorkTime = (TextView) findViewById(R.id.tvWorkTime);
        tvShortRest = (TextView) findViewById(R.id.tvShortRest);
        tvLongRest = (TextView) findViewById(R.id.tvLongRest);
        sbWorkTime = (AppCompatSeekBar) findViewById(R.id.sbWorkTime);
        sbShortRest = (AppCompatSeekBar) findViewById(R.id.sbShortRest);
        sbLongRest = (AppCompatSeekBar) findViewById(R.id.sbLongRest);

        cbVibrateAlarm = (CheckBox) findViewById(R.id.cbVibrateAlarm);
        cbRingtoneAlarm = (CheckBox) findViewById(R.id.cbRingtoneAlarm);

        //初始化sharedPreference及editor：API内置如果不存在则自动创建一个
//        sharedPreferences = getSharedPreferences("TomaotSetting", MODE_PRIVATE);
//        editor = sharedPreferences.edit();
        int tempInt = SpTool.getInt("workTime", 0);
        if (tempInt == 0) {//表明是第一次生成这个sharedPreference，则调用默认配置显示（这里就是只对栏目名做显示处理）
            String htmlString = "<font>工作时间：</font><font color=\"#1E90FF\">" + "25" + "</font><font>分钟</font>";
            tvWorkTime.setText(Html.fromHtml(htmlString));

            String htmlString2 = "<font>短休息：</font><font color=\"#1E90FF\">" + "5" + "</font><font>分钟</font>";
            tvShortRest.setText(Html.fromHtml(htmlString2));

            String htmlString3 = "<font>长休息：</font><font color=\"#1E90FF\">" + "20" + "</font><font>分钟</font>";
            tvLongRest.setText(Html.fromHtml(htmlString3));
        } else {//如果sharedPreference已经有值，则使用里面的值进行初始化的显示
            //初始化标题栏
            String htmlString = "<font>工作时间：</font><font color=\"#1E90FF\">" +String.valueOf(SpTool.getInt("workTime", 25)) + "</font><font>分钟</font>";
            tvWorkTime.setText(Html.fromHtml(htmlString));

            String htmlString2 = "<font>短休息：</font><font color=\"#1E90FF\">" + String.valueOf(SpTool.getInt("shortRestTime", 5)) + "</font><font>分钟</font>";
            tvShortRest.setText(Html.fromHtml(htmlString2));

            String htmlString3 = "<font>长休息：</font><font color=\"#1E90FF\">" + String.valueOf(SpTool.getInt("longRestTime", 20)) + "</font><font>分钟</font>";
            tvLongRest.setText(Html.fromHtml(htmlString3));

            //初始化进度条
            sbWorkTime.setProgress(SpTool.getInt(StaticData.SPWORKTIME, 25));
            sbShortRest.setProgress(SpTool.getInt(StaticData.SPSHORTRESTTIME, 5));
            sbLongRest.setProgress(SpTool.getInt(StaticData.SPLONGRESTTIME, 20));

            cbVibrateAlarm.setChecked(SpTool.getBoolean(StaticData.SPVIBRATEALARM, true));
            cbRingtoneAlarm.setChecked(SpTool.getBoolean(StaticData.SPRINGTONEALARM, true));
        }

        sbWorkTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String htmlString = "<font>工作时间：</font><font color=\"#1E90FF\">" + String.valueOf(progress) + "</font><font>分钟</font>";
                tvWorkTime.setText(Html.fromHtml(htmlString));
                if(progress == 0){//不允许设置为0 ，否则默认为25
                    sbWorkTime.setProgress(25);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbShortRest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String htmlString = "<font>短休息：</font><font color=\"#1E90FF\">" + String.valueOf(progress) + "</font><font>分钟</font>";
                tvShortRest.setText(Html.fromHtml(htmlString));
                if(progress == 0){//不允许设置为0，否则默认为5
                    sbShortRest.setProgress(5);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbLongRest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String htmlString = "<font>长休息：</font><font color=\"#1E90FF\">" + String.valueOf(progress) + "</font><font>分钟</font>";
                tvLongRest.setText(Html.fromHtml(htmlString));
                if(progress == 0){//不允许设置为0 ，否则默认为20
                    sbLongRest.setProgress(20);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void initListeners(){
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
