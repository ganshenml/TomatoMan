package com.example.ganshenml.tomatoman.act;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.util.VerifyUtils;
import com.example.ganshenml.tomatoman.view.ClearEditTextView;

public class RegisterAct extends AppCompatActivity {
    private RelativeLayout rlLogin;
    private TextView tvVerifyWarn;
    private ClearEditTextView ctUsername;
    private ClearEditTextView ctPassword;
    private ClearEditTextView ctVerifyPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_register);

        initViews();

        listenerMethod();

    }


    //--------------------------------------------------------------以下为自定义方法-----------------------------------------------
    private void initViews() {
        rlLogin = (RelativeLayout) findViewById(R.id.rlLogin);
        ctUsername = (ClearEditTextView) findViewById(R.id.ctUsername);
        ctPassword = (ClearEditTextView) findViewById(R.id.ctPassword);
        ctVerifyPassword = (ClearEditTextView) findViewById(R.id.ctVerifyPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvVerifyWarn = (TextView) findViewById(R.id.tvVerifyWarn);

    }

    private void listenerMethod() {

        //注册按钮：注册事件
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.对用户名，两次密码验证，如果通过返回true；

                //2.将新增用户的信息注册进入数据库，并进入验证邮箱的Activity

            }
        });

        //用户名一栏的监听事件：进行合法性校验
        ctUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count != 0) {
                    String tempStr = VerifyUtils.verifyName(s.toString());
                    tvVerifyWarn.setText(tempStr);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //判断软键盘是否打开——>如果打开，则在点击界面后进行隐藏
        rlLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(rlLogin.getWindowToken(), 0);
                }
            }
        });
    }


}
