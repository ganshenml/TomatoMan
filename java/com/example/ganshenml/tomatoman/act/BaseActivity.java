package com.example.ganshenml.tomatoman.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ganshenml.tomatoman.util.BmobTool;
import com.facebook.drawee.backends.pipeline.Fresco;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        BmobTool.init(getApplication());
//
//        initViews();
//        initData();
//        initDataViews();
    }

//    protected void initViews(){}
//
//    protected void initData(){}
//
//    protected void initDataViews(){}
}
