package com.example.ganshenml.tomatoman.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.adapter.RankListViewAdapter;
import com.example.ganshenml.tomatoman.bean.User;

import java.util.ArrayList;
import java.util.List;

public class RankAct extends AppCompatActivity {
    private ListView listView;
    private List<User> list = new ArrayList<>();
    private Button thisWeekBtn, totalRankBtn;
    private Toolbar rankTb;
    private ImageView backIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        initViews();
        initListeners();
    }

    private void initViews() {
        rankTb = (Toolbar) findViewById(R.id.rankTb);
        rankTb.setTitle("");
        setSupportActionBar(rankTb);

        thisWeekBtn = (Button) findViewById(R.id.thisWeekBtn);
        totalRankBtn = (Button) findViewById(R.id.totalRankBtn);
        backIv = (ImageView) findViewById(R.id.backIv);


        listView = (ListView) findViewById(R.id.lvRank);

        //初始化list数据
        initList();
        RankListViewAdapter rankListViewAdapter = new RankListViewAdapter(getApplication(), list);
        listView.setAdapter(rankListViewAdapter);

        //初始化toolBar状态
        thisWeekBtn.setSelected(true);
        totalRankBtn.setSelected(false);

    }

    private void initListeners() {
        thisWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //初始化toolBar状态
                thisWeekBtn.setSelected(true);
                totalRankBtn.setSelected(false);

            }
        });

        totalRankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //初始化toolBar状态
                thisWeekBtn.setSelected(false);
                totalRankBtn.setSelected(true);

            }
        });

        //退出
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    //初始化list项数据
    private void initList() {
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setUserId(i + 1);
            user.setImageId(R.drawable.logo_person);
            user.setUserName("用户名" + i);
            user.setUserTomatoNum(i + 20);
            list.add(user);
        }
    }

}
