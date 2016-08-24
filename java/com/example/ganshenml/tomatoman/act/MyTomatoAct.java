package com.example.ganshenml.tomatoman.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.adapter.TomatoRecordAdapter;
import com.example.ganshenml.tomatoman.bean.Person;
import com.example.ganshenml.tomatoman.bean.TomatoRecord;
import com.example.ganshenml.tomatoman.bean.beant.TomatoRecordT;
import com.example.ganshenml.tomatoman.callback.HttpCallback;
import com.example.ganshenml.tomatoman.net.NetRequest;
import com.example.ganshenml.tomatoman.tool.ConstantCode;
import com.example.ganshenml.tomatoman.tool.DbTool;
import com.example.ganshenml.tomatoman.tool.LogTool;
import com.example.ganshenml.tomatoman.tool.SpTool;
import com.example.ganshenml.tomatoman.tool.ThreadTool;
import com.example.ganshenml.tomatoman.view.SimpleListView;

import java.util.ArrayList;
import java.util.List;

public class MyTomatoAct extends BaseActivity {
    private final String TAG = "MyTomatoAct";
    private Toolbar myTomatoTb;
    private ImageView backIv, ivMyTomato, ivMyTomato2;
    private ListView myTomatoRecordLv;
    private TomatoRecordAdapter tomatoRecordAdapter;
    private List<TomatoRecordT> tomatoRecordArrayList;
    private ScrollView myTomatoSv;
    private TextView myTomatoNumTv, myTomatoTimeTv,efficiencyStrTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tomato);

        initViews();
        initDataViews();
        initListeners();
    }

    private void initViews() {
        myTomatoTb = (Toolbar) findViewById(R.id.myTomatoTb);
        myTomatoTb.setTitle("");
        setSupportActionBar(myTomatoTb);

        myTomatoNumTv = (TextView) findViewById(R.id.myTomatoNumTv);
        myTomatoTimeTv = (TextView) findViewById(R.id.myTomatoTimeTv);
        efficiencyStrTv = (TextView) findViewById(R.id.efficiencyStrTv);
        backIv = (ImageView) findViewById(R.id.backIv);
        ivMyTomato = (ImageView) findViewById(R.id.ivMyTomato);
        ivMyTomato2 = (ImageView) findViewById(R.id.ivMyTomato2);

        myTomatoRecordLv = (ListView) findViewById(R.id.myTomatoRecordLv);

        //展现“超人”动画
        showAnimations();
    }

    private void initDataViews() {
        initArrayList();
        tomatoRecordAdapter = new TomatoRecordAdapter(this, tomatoRecordArrayList);
        myTomatoRecordLv.setAdapter(tomatoRecordAdapter);
        tomatoRecordAdapter.notifyDataSetChanged();

        //本地是否有TomatoRecord数据：无：从服务器取100条保存；有：取出服务器最新的数据判断是否新于本地——>是，获取所有这部分新的数据，保存在本地
        if (DbTool.isHasTomatoRecordData()) {
            LogTool.log(LogTool.Aaron, TAG + "initDataViews 判断本地有数据，开始请求服务端最新数据");
            requestTomatoRecordLatestDataAndSave();
        } else {
            requestWholeTomatoRecordDataAndSave();
            LogTool.log(LogTool.Aaron, TAG + "initDataViews 判断本地没有数据，开始请求服务端数据");
        }

        //将本地数据分页(20条)倒序显示出来++++++++++++++++++++++++++++++分页暂时未考虑


        /**
         * 网络请求总番茄数量
         */
        NetRequest.returnTomatoNumData(Person.getCurrentUser(Person.class), new HttpCallback() {
            @Override
            public void onSuccess(Object data, String resultStr) {
                LogTool.log(LogTool.Aaron,"MyTomatoAct NetRequest 请求番茄数量返回成功");
                if (resultStr != null) {
                    LogTool.log(LogTool.Aaron,"MyTomatoAct NetRequest 请求番茄数量返回字符串为： "+resultStr);
                    myTomatoNumTv.setText(resultStr);
                }
            }
        });

        /**
         * 网络请求总番茄时间
         */
        NetRequest.returnTomatoTimeData(Person.getCurrentUser(Person.class), new HttpCallback() {
            @Override
            public void onSuccess(Object data, String resultStr) {
                if (resultStr != null) {
                    myTomatoTimeTv.setText(resultStr);
                }
            }
        });


        /**
         * 网络请求总番茄高效时间
         */
        NetRequest.returnefficientTimeData(Person.getCurrentUser(Person.class), new HttpCallback() {
            @Override
            public void onSuccess(Object data, String resultStr) {
                if (resultStr != null) {
                    String htmlString = "<font>（小时，含 </font><font color=\"#52A5FF\">" + resultStr + "</font><font> 小时高效超人时间）</font>";
                    efficiencyStrTv.setText(Html.fromHtml(htmlString));
                }
            }
        });


    }

    private void initListeners() {
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myTomatoRecordLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyTomatoAct.this, TomatoCompleteAct.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("tomatoRecordT", tomatoRecordArrayList.get(position));
                intent.putExtras(bundle);
                intent.setFlags(ConstantCode.ACTIVITY_FROM_MYTOMATO_CODE);
                startActivity(intent);
            }
        });
    }

    private void initArrayList() {
        tomatoRecordArrayList = new ArrayList<>();
        tomatoRecordArrayList = DbTool.returnWholeTomatoRecordData();
    }


    //____________________________________________________________________________________

    /**
     * 从服务器端获取100条TomatoRecord数据并保存至本地
     */
    private void requestWholeTomatoRecordDataAndSave() {
        NetRequest.requestWholeTomatoRecordData(new HttpCallback<TomatoRecord>() {
            @Override
            public void onSuccess(List<TomatoRecord> data) {
                LogTool.log(LogTool.Aaron, TAG + "requestWholeTomatoRecordData 服务端进行了返回");
                saveTomatoRecordData(data);
            }
        });
    }

    /**
     * 取出服务器最新的TomatoRecord数据并保存至本地
     */
    private void requestTomatoRecordLatestDataAndSave() {
        //取出服务器相较于本地存储的最新的数据

        String latestDateStr = null;
        TomatoRecordT t = DbTool.returnLatestTomatoRecordData();
        if (t != null) {
            latestDateStr = t.getCreatedAt();
        }
        LogTool.log(LogTool.Aaron, TAG + "requestTomatoRecordLatestDataAndSave 服务端与本地进行的比较日期是： " + latestDateStr);

        NetRequest.requestTomatoRecordLatestData(latestDateStr, new HttpCallback<TomatoRecord>() {
            @Override
            public void onSuccess(List<TomatoRecord> data) {
                LogTool.log(LogTool.Aaron, TAG + "requestTomatoRecordLatestDataAndSave 服务端进行了返回");

                if (data != null) {//如果不为空，则表示有最新的数据，则进行本地保存
                    LogTool.log(LogTool.Aaron, TAG + "requestTomatoRecordLatestDataAndSave 服务端数据不为空");
                    saveTomatoRecordData(data);
                }
            }
        });
    }

    /**
     * 1.保存TomatoRecord数据至本地；2.获取本地数据并显示在界面
     *
     * @param data
     */
    private void saveTomatoRecordData(List<TomatoRecord> data) {
        LogTool.log(LogTool.Aaron, TAG + "saveTomatoRecordData 开始进行服务器端数据的本地保存");

        DbTool.saveTomatoRecordToLocal(data);
        tomatoRecordArrayList.clear();
        tomatoRecordArrayList = DbTool.returnWholeTomatoRecordData();
        if (tomatoRecordAdapter != null) {
            tomatoRecordAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 展现“超人”动画
     */
    private void showAnimations() {

//        ivMyTomato2.setVisibility(View.VISIBLE);
//        AnimationSet animationSet = new AnimationSet(true);
//        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
//        scaleAnimation.setDuration(1500);
//        animationSet.addAnimation(scaleAnimation);
//
//        TranslateAnimation translateAnimation = new TranslateAnimation(-getResources().getDisplayMetrics().widthPixels, 0, getResources().getDisplayMetrics().heightPixels, 0);
//        translateAnimation.setDuration(1500);
//        animationSet.addAnimation(translateAnimation);
//
//        ivMyTomato2.setAnimation(animationSet);
//
//        ThreadTool.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                ivMyTomato2.setVisibility(View.INVISIBLE);
//                ivMyTomato.setVisibility(View.VISIBLE);
//            }
//        }, 1600);

        TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 0, -300, 0);
        translateAnimation2.setDuration(1000);
        ivMyTomato.setAnimation(translateAnimation2);
        ivMyTomato.setVisibility(View.VISIBLE);

    }
}
