package com.example.ganshenml.tomatoman.act;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.ganshenml.tomatoman.R;

public class MyTomatoAct extends BaseActivity {
    private Toolbar myTomatoTb;
    private ImageView backIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tomato);

        initViews();
        initListeners();
    }

    private void initViews(){
        myTomatoTb = (Toolbar) findViewById(R.id.myTomatoTb);
        myTomatoTb.setTitle("");
        setSupportActionBar(myTomatoTb);

        backIv = (ImageView) findViewById(R.id.backIv);
    }

    private void initListeners(){
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
