package com.example.ganshenml.tomatoman.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.act.SearchUserAct;
import com.example.ganshenml.tomatoman.util.ShowDrawerLayout;
import com.example.ganshenml.tomatoman.view.SimpleListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ganshenml on 2016/4/1.
 */
public class MyFriendsFragment extends Fragment {
    Activity activity = getActivity();
    View view;
    SimpleListView lvMyFriends;
    List<Map<String,String>> list = new ArrayList<>();
    ScrollView svMyFriends;
    TextView tvAddFriends;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_friends,container,false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //初始化view，方便以后使用
        view = getView();

        //为左侧汉堡菜单设置点击监听事件：显示DrawerLayout
        ShowDrawerLayout.showDrawerLayout(getActivity(), R.id.tbMyFriends, "好友", R.id.drawer_layout);

        for (int i = 0; i < 20; i++) {
            Map<String,String> map = new HashMap<String,String>();
            map.put("userName",String.valueOf(i));
            map.put("userImageId",String.valueOf(R.drawable.logo_person));
            map.put("userIntroduction","这是第"+(i+1)+"的简介");
            list.add(map);
        }

        lvMyFriends = (SimpleListView) view.findViewById(R.id.lvMyFriends);
        svMyFriends = (ScrollView) view.findViewById(R.id.svMyFriends);
        svMyFriends.post(new Runnable() {
            @Override
            public void run() {
                svMyFriends.smoothScrollTo(0, 0);
            }
        });

        //填充列表
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), list,R.layout.layout_item_person,new String[]{"userName","userImageId","userIntroduction"},new int[]
                {R.id.tvUserNameItem,R.id.ivUserImageItem,R.id.tvUserIntroductionItem});
        lvMyFriends.setAdapter(simpleAdapter);

        //点击“加好友”进入加好友的页面SearchUserAct
        tvAddFriends = (TextView) getActivity().findViewById(R.id.tvAddFriends);
        tvAddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchUserAct.class);
                startActivity(intent);
            }
        });

    }
}
