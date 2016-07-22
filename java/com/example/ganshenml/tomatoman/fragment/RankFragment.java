package com.example.ganshenml.tomatoman.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.adapter.RankListViewAdapter;
import com.example.ganshenml.tomatoman.bean.User;
import com.example.ganshenml.tomatoman.util.LogTool;
import com.example.ganshenml.tomatoman.util.ShowDrawerLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganshenml on 2016/4/1.
 */
public class RankFragment extends Fragment {
    private final String TAG = "RankFragment";
    private View view;
    private Toolbar toolbar;
    private Button btnThisWeek, btnTotalRank;
    private ListView listView;
    private List<User> list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rank, container, false);
        listView = (ListView) view.findViewById(R.id.lvRank);
        //初始化list数据
        initList();
        RankListViewAdapter rankListViewAdapter = new RankListViewAdapter(getContext(), list);
        listView.setAdapter(rankListViewAdapter);
        return view;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.toolbar = (Toolbar) getActivity().findViewById(R.id.tbRank);
        btnThisWeek = (Button) this.toolbar.findViewById(R.id.btnThisWeek);
        btnTotalRank = (Button) this.toolbar.findViewById(R.id.btnTotalRank);
        btnTotalRank.setSelected(false);
        btnThisWeek.setSelected(true);

        LogTool.log(LogTool.Aaron,TAG+" btn样式初始化设置已完成");

        btnTotalRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnTotalRank.isSelected()) {//如果从“本周”选项切换过来：1.改变选项颜色；2.显示对应的fragment

                    btnTotalRank.setSelected(true);
                    btnThisWeek.setSelected(false);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.rlHome, new TotalRankFragment());
                    transaction.commit();
                }
            }
        });

        //为左侧汉堡菜单设置点击监听事件：显示DrawerLayout
        ShowDrawerLayout.showDrawerLayout(getActivity(), R.id.tbRank, "", R.id.drawer_layout);
    }


}
