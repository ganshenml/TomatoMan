package com.example.ganshenml.tomatoman.act;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.tool.LogTool;

import org.w3c.dom.Text;

public class PasswordAct extends AppCompatActivity {
    private Toolbar passwordTb;
    private ImageView backIv;
    private Button copyEmailBtn;
    private TextView emailTv ;
    private ClipboardManager myClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        initViews();
        initDataViews();
        initListeners();
    }

    private void initViews(){
        passwordTb = (Toolbar) findViewById(R.id.passwordTb);
        passwordTb.setTitle("");
        setSupportActionBar(passwordTb);

        backIv = (ImageView)findViewById(R.id.backIv);
        copyEmailBtn = (Button)findViewById(R.id.copyEmailBtn);
        emailTv = (TextView)findViewById(R.id.emailTv);

        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
    }

    private void initDataViews(){
        copyEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData myClip = ClipData.newPlainText("text", emailTv.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(PasswordAct.this,"地址复制成功",Toast.LENGTH_LONG).show();
            }
        });

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

