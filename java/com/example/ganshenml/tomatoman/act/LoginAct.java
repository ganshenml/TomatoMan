package com.example.ganshenml.tomatoman.act;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.Person;
import com.example.ganshenml.tomatoman.util.LogTool;
import com.example.ganshenml.tomatoman.util.ToActivityPage;
import com.example.ganshenml.tomatoman.view.ClearEditTextView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginAct extends BaseActivity {
    private String usernameTagStr;//注册成功后会将注册成功的用户名返回（依次作为界面显示）

    public final String TAG = "LoginAct";
    private RelativeLayout rlLogin;
    private ClearEditTextView ctUsername;
    private ClearEditTextView ctPassword;
    private TextView registerTv;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        listenerMethod();
    }

    public void setUsernameTagStr(String usernameTagStr){
        this.usernameTagStr = usernameTagStr;
    }
    //----------------------------------------------------------以下为自定义方法-------------------------------------------------

    private void initViews() {
        rlLogin = (RelativeLayout) findViewById(R.id.rlLogin);
        ctUsername = (ClearEditTextView) findViewById(R.id.ctUsername);
        ctPassword = (ClearEditTextView) findViewById(R.id.ctPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        registerTv = (TextView) findViewById(R.id.registerTv);

        //如果是从注册页面返回过来，则将username一栏填充数据，并调出软键盘
        showViewIfFromRegisterAct(usernameTagStr);
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
                Person person = new Person();
                String userNameStr = ctUsername.getText().toString().trim();
                String userPassStr = ctPassword.getText().toString().trim();
                LogTool.log(LogTool.Aaron, TAG + " 点击了登录按钮 参数为：" + userNameStr + " " + userPassStr);

                person.setUsername(userNameStr);
                person.setPassword(userPassStr);

                person.login(new SaveListener<Person>() {
                    @Override
                    public void done(Person person, BmobException e) {
                        if (e == null) {
                            LogTool.log(LogTool.Aaron, TAG + " 登录成功");
                            Toast.makeText(LoginAct.this, "登录成功", Toast.LENGTH_SHORT).show();
                            //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                            //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                            ToActivityPage.turnToSimpleAct(LoginAct.this, MainActivity.class);//跳转至MainAct界面
                        } else {
                            Toast.makeText(LoginAct.this, e.toString(), Toast.LENGTH_LONG).show();
                            LogTool.log(LogTool.Aaron, TAG + " 登录失败： " + e.toString());
                        }
                    }
                });

            }
        });

        //注册
        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToActivityPage.turnToSimpleAct(LoginAct.this, RegisterAct.class);//跳转至注册界面
            }
        });
    }


    //如果是从注册页面返回过来，则将username一栏填充数据，并调出软键盘
    private void showViewIfFromRegisterAct(String usernameTagStr) {
        if (usernameTagStr == null) {
            return;
        }

        //usernameTagStr不为空，则表示是从注册页面注册成功后的返回
        ctUsername.setText(usernameTagStr);
        ctPassword.setFocusable(true);
        ctPassword.setFocusableInTouchMode(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
