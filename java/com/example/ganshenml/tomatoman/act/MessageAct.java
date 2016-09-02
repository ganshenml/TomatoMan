package com.example.ganshenml.tomatoman.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.tool.StringTool;

public class MessageAct extends AppCompatActivity {
    private Toolbar messsageTb;
    private ImageView backIv;
    private TextView messageContentTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initViews();
        initDataViews();
        initListeners();
    }

    private void initViews(){
        messsageTb = (Toolbar) findViewById(R.id.messsageTb);
        messsageTb.setTitle("");
        setSupportActionBar(messsageTb);

        backIv = (ImageView)findViewById(R.id.backIv);
        messageContentTv = (TextView)findViewById(R.id.messageContentTv);
    }

    private void initDataViews(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String contentStr = bundle.getString("content");
        if(!StringTool.isEmpty(contentStr)){
            messageContentTv.setText(contentStr);
        }
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
