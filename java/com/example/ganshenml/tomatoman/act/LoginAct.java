package com.example.ganshenml.tomatoman.act;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.Person;
import com.example.ganshenml.tomatoman.view.ClearEditTextView;

import cn.bmob.v3.listener.SaveListener;

public class LoginAct extends AppCompatActivity {
    private RelativeLayout rlLogin;
    private ClearEditTextView ctUsername;
    private ClearEditTextView ctPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        listenerMethod();
    }


    //----------------------------------------------------------以下为自定义方法-------------------------------------------------

    private void initViews() {
        rlLogin = (RelativeLayout) findViewById(R.id.rlLogin);
        ctUsername = (ClearEditTextView) findViewById(R.id.ctUsername);
        ctPassword = (ClearEditTextView) findViewById(R.id.ctPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    private void listenerMethod() {
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

        //登录
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Person person = new Person();
                person.setName("1");
                person.setAddress("深圳");

                person.save(LoginAct.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(LoginAct.this, person.getObjectId().toString(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(LoginAct.this,"用户名错误或密码不匹配",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


    }


}
