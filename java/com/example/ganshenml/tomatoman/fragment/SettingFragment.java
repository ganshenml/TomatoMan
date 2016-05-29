package com.example.ganshenml.tomatoman.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.act.MyTomatoAct;
import com.example.ganshenml.tomatoman.act.TomatoSettingAct;
import com.example.ganshenml.tomatoman.util.ShowDrawerLayout;

/**
 * Created by ganshenml on 2016/3/31.
 */
public class SettingFragment extends Fragment {
    View llTomatoSetting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        llTomatoSetting = getView().findViewById(R.id.llTomatoSetting);
        llTomatoSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TomatoSettingAct.class);
                startActivity(intent);
            }
        });

        //为左侧汉堡菜单设置点击监听事件：显示DrawerLayout
        ShowDrawerLayout.showDrawerLayout(getActivity(), R.id.tbSetting, "设置", R.id.drawer_layout);
    }
}
