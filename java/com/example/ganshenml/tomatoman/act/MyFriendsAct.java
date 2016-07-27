package com.example.ganshenml.tomatoman.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.data.StaticData;
import com.example.ganshenml.tomatoman.util.ShowDrawerLayout;
import com.example.ganshenml.tomatoman.view.SimpleListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFriendsAct extends AppCompatActivity {

    private SimpleListView lvMyFriends;
    private List<Map<String, String>> list = new ArrayList<>();
    private ScrollView svMyFriends;
    private TextView addFriendsTv;
    private Toolbar myFriendsTb;
    private ImageView backIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        initViews();
        initListeners();
    }

    private void initViews() {
        myFriendsTb = (Toolbar) findViewById(R.id.myFriendsTb);
        myFriendsTb.setTitle("");
        setSupportActionBar(myFriendsTb);

        for (int i = 0; i < 20; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("userName", String.valueOf(i));
            map.put("userImageId", String.valueOf(R.drawable.logo_person));
            map.put("userIntroduction", "这是第" + (i + 1) + "的简介");
            list.add(map);
        }

        lvMyFriends = (SimpleListView) findViewById(R.id.lvMyFriends);
        svMyFriends = (ScrollView) findViewById(R.id.svMyFriends);
        backIv = (ImageView) findViewById(R.id.backIv);


//        svMyFriends.post(new Runnable() {
//            @Override
//            public void run() {
//                svMyFriends.smoothScrollTo(0, 0);
//            }
//        });

        //填充列表
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.layout_item_person, new String[]{"userName", "userImageId", "userIntroduction"}, new int[]
                {R.id.tvUserNameItem, R.id.ivUserImageItem, R.id.tvUserIntroductionItem});
        lvMyFriends.setAdapter(simpleAdapter);

        //点击“加好友”进入加好友的页面SearchUserAct
        addFriendsTv = (TextView) findViewById(R.id.addFriendsTv);
        addFriendsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFriendsAct.this, SearchUserAct.class);
                startActivity(intent);
            }
        });

    }


    private  void initListeners(){
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}






