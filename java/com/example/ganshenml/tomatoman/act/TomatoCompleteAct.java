package com.example.ganshenml.tomatoman.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.util.ViewUtils;

import static android.graphics.Color.BLACK;

/*
    番茄计时完成的页面，进行各项数据的统计
 */
public class TomatoCompleteAct extends BaseActivity {
    Button btnTomatoComplete;
    private static final int RESULTCODE_TO_MAIN = 101;
    private Toolbar tbToolbar_public;//公用toolbar
    private TextView tvTitle_public;//公用toolbar标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_complete);

        initViews();
        listenerMethod();
    }

    //对回退事件做弹窗处理
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){//如果点击了回退
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示").setMessage("确定不保存当前数据吗！").setCancelable(true)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//点击确定：不保存当前数据，回到首页
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(TomatoCompleteAct.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            return  false;
        }
        return super.onKeyDown(keyCode, event);
    }

    //-------------------------------------------------------以下为自定义方法-------------------------------------------
    private void initViews() {
        btnTomatoComplete = (Button) findViewById(R.id.btnTomatoComplete);

        tbToolbar_public = (Toolbar) findViewById(R.id.tbToolbar_public);
        tvTitle_public = (TextView) findViewById(R.id.tvTitle_public);
        setSupportActionBar(tbToolbar_public);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示默认的标题

        ViewUtils.setToolbar(TomatoCompleteAct.this,tbToolbar_public,tvTitle_public);//使用自定义的工具类方法设置toolbar的样式

    }


    private void listenerMethod() {

        //点击“完成”按钮：1.完成数据保存；2.退出当前Activity；3.回到首页；
        btnTomatoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULTCODE_TO_MAIN);
                //1.完成数据保存

                //2.退出当前Activity
                finish();

                //3.回到首页
                Intent intent = new Intent(TomatoCompleteAct.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //点击“完成并分享”
    }
}
