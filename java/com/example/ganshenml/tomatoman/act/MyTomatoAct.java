package com.example.ganshenml.tomatoman.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.adapter.TomatoRecordAdapter;
import com.example.ganshenml.tomatoman.bean.TomatoRecord;
import com.example.ganshenml.tomatoman.bean.beant.TomatoRecordT;
import com.example.ganshenml.tomatoman.callback.HttpCallback;
import com.example.ganshenml.tomatoman.net.NetRequest;
import com.example.ganshenml.tomatoman.tool.ConstantCode;
import com.example.ganshenml.tomatoman.tool.DbTool;
import com.example.ganshenml.tomatoman.tool.LogTool;
import com.example.ganshenml.tomatoman.view.SimpleListView;

import java.util.ArrayList;
import java.util.List;

public class MyTomatoAct extends BaseActivity {
    private final String TAG = "MyTomatoAct";
    private Toolbar myTomatoTb;
    private ImageView backIv;
    private SimpleListView myTomatoRecordLv;
    private TomatoRecordAdapter tomatoRecordAdapter;
    private List<TomatoRecordT> tomatoRecordArrayList;

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

        backIv = (ImageView) findViewById(R.id.backIv);

        myTomatoRecordLv = (SimpleListView) findViewById(R.id.myTomatoRecordLv);
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

        //将本地数据分页(20条)倒序显示出来

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
                bundle.putSerializable("tomatoRecordT",tomatoRecordArrayList.get(position));
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
        LogTool.log(LogTool.Aaron, TAG + "requestTomatoRecordLatestDataAndSave 服务端与本地进行的比较日期是： "+ latestDateStr);

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
}
