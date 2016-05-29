package com.example.ganshenml.tomatoman.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.util.ShowDrawerLayout;


public class MyTomatoFragment extends Fragment {
    View view;
    ScrollView svMyTomato;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar tbMyTomato;
    ImageView ivHumburger_MyTomato;

    public MyTomatoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("MyTomatoFragment", "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("MyTomatoFragment", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("MyTomatoFragment","onCreateView" );
        return inflater.inflate(R.layout.fragment_my_tomato, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view = getView();

        svMyTomato = (ScrollView) view.findViewById(R.id.svMyTomato);
        svMyTomato.post(new Runnable() {
            @Override
            public void run() {
                svMyTomato.smoothScrollTo(0, 0);
            }
        });

        //为左侧汉堡菜单设置点击监听事件：显示DrawerLayout
        ShowDrawerLayout.showDrawerLayout(getActivity(), R.id.tbMyTomato, "我的番茄", R.id.drawer_layout);

        Log.e("MyTomatoFragment", "onActivityCreated");
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.e("MyTomatoFragment","onStart" );
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("MyTomatoFragment", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("MyTomatoFragment", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("MyTomatoFragment", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("MyTomatoFragment", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("MyTomatoFragment", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("MyTomatoFragment", "onDetach");
    }
}
