package com.example.ganshenml.tomatoman.act;

import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;

public class TomatoSettingAct extends AppCompatActivity {

    private TextView tvWorkTime, tvShortRest, tvLongRest;
    private AppCompatSeekBar sbWorkTime, sbShortRest, sbLongRest;
    private CheckBox cbVibrateAlarm, cbRingtoneAlarm;//振动提醒和响铃提醒的设置
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_tomato_setting);

        initViews();
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
        editor.putInt("workTime", sbWorkTime.getProgress());
        editor.putInt("shortRestTime", sbShortRest.getProgress());
        editor.putInt("longRestTime", sbLongRest.getProgress());

        editor.putBoolean("vibrateAlarm", cbVibrateAlarm.isChecked());
        editor.putBoolean("ringtoneAlarm", cbRingtoneAlarm.isChecked());

        editor.commit();
    }

    //-------------------------------------------------------------------以下为自定义方法-----------------------------------------------------
    //初始化控件
    private void initViews() {
        tvWorkTime = (TextView) findViewById(R.id.tvWorkTime);
        tvShortRest = (TextView) findViewById(R.id.tvShortRest);
        tvLongRest = (TextView) findViewById(R.id.tvLongRest);
        sbWorkTime = (AppCompatSeekBar) findViewById(R.id.sbWorkTime);
        sbShortRest = (AppCompatSeekBar) findViewById(R.id.sbShortRest);
        sbLongRest = (AppCompatSeekBar) findViewById(R.id.sbLongRest);

        cbVibrateAlarm = (CheckBox) findViewById(R.id.cbVibrateAlarm);
        cbRingtoneAlarm = (CheckBox) findViewById(R.id.cbRingtoneAlarm);

        //初始化sharedPreference及editor：API内置如果不存在则自动创建一个
        sharedPreferences = getSharedPreferences("TomaotSetting", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        int tempInt = sharedPreferences.getInt("workTime", 0);
        if (tempInt == 0) {//表明是第一次生成这个sharedPreference，则调用默认配置显示（这里就是只对栏目名做显示处理）
            String htmlString = "<font>工作时间：</font><font color=\"#1E90FF\">" + "25" + "</font><font>分钟</font>";
            tvWorkTime.setText(Html.fromHtml(htmlString));

            String htmlString2 = "<font>短休息：</font><font color=\"#1E90FF\">" + "5" + "</font><font>分钟</font>";
            tvShortRest.setText(Html.fromHtml(htmlString2));

            String htmlString3 = "<font>长休息：</font><font color=\"#1E90FF\">" + "20" + "</font><font>分钟</font>";
            tvLongRest.setText(Html.fromHtml(htmlString3));
        } else {//如果sharedPreference已经有值，则使用里面的值进行初始化的显示
            //初始化标题栏
            String htmlString = "<font>工作时间：</font><font color=\"#1E90FF\">" +String.valueOf(sharedPreferences.getInt("workTime", 25)) + "</font><font>分钟</font>";
            tvWorkTime.setText(Html.fromHtml(htmlString));

            String htmlString2 = "<font>短休息：</font><font color=\"#1E90FF\">" + String.valueOf(sharedPreferences.getInt("shortRestTime", 5)) + "</font><font>分钟</font>";
            tvShortRest.setText(Html.fromHtml(htmlString2));

            String htmlString3 = "<font>长休息：</font><font color=\"#1E90FF\">" + String.valueOf(sharedPreferences.getInt("longRestTime", 20)) + "</font><font>分钟</font>";
            tvLongRest.setText(Html.fromHtml(htmlString3));

            //初始化进度条
            sbWorkTime.setProgress(sharedPreferences.getInt("workTime", 25));
            sbShortRest.setProgress(sharedPreferences.getInt("shortRestTime", 5));
            sbLongRest.setProgress(sharedPreferences.getInt("longRestTime", 20));

            cbVibrateAlarm.setChecked(sharedPreferences.getBoolean("vibrateAlarm", true));
            cbRingtoneAlarm.setChecked(sharedPreferences.getBoolean("ringtoneAlarm", true));
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

}
