package com.example.ganshenml.tomatoman.act;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.Person;
import com.example.ganshenml.tomatoman.tool.CommonUtils;
import com.example.ganshenml.tomatoman.tool.LogTool;
import com.example.ganshenml.tomatoman.tool.ThreadTool;
import com.example.ganshenml.tomatoman.tool.ToActivityPage;
import com.example.ganshenml.tomatoman.tool.VerifyUtils;
import com.example.ganshenml.tomatoman.view.ClearEditTextView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterAct extends BaseActivity {
    private final String TAG = "RegisterAct";
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
                if(!CommonUtils.judgeNetWork(RegisterAct.this)){//如果当前网络不可用
                    return;
                }

                //1.对用户名，两次密码验证，如果通过返回true；
                if (!isValidate()) {
                    return;
                }

                //2.将新增用户的信息注册进入数据库(并进入验证邮箱的Activity，这步以后实现_Aaron_future)
                final Person person1 = new Person();
                final String usernameTemp = ctUsername.getText().toString().trim();
                final String passwordTemp = ctPassword.getText().toString().trim();
                person1.setUsername(usernameTemp);
                person1.setPassword(passwordTemp);

                person1.signUp(new SaveListener<Person>() {
                    @Override
                    public void done(Person person, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterAct.this, "注册成功", Toast.LENGTH_LONG).show();

                            //进行默认的登录操作（登录成功：进入MainAct，否则提升进入登录页）
                            ThreadTool.runOnNewThread(new Runnable() {
                                @Override
                                public void run() {
                                    toLogin(person1);
                                }
                            });

                        } else {
                            Toast.makeText(RegisterAct.this, "注册失败:" + e.toString(), Toast.LENGTH_LONG).show();
                            LogTool.log(LogTool.Aaron, TAG + " 注册失败：" + e.toString());
                        }
                    }
                });
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

    /**
     * 对用户名、两次密码合法性的检验
     *
     * @return
     */
    private boolean isValidate() {

        String usernameTemp = ctUsername.getText().toString().trim();
        if (usernameTemp == null || usernameTemp.length() == 0) {
            Toast.makeText(RegisterAct.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        String passStr01 = ctPassword.getText().toString().trim();
        String passStr02 = ctVerifyPassword.getText().toString().trim();

        if (passStr01 == null || passStr01.length() == 0) {
            Toast.makeText(RegisterAct.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!passStr01.equals(passStr02)) {
            Toast.makeText(RegisterAct.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 进行默认的登录操作（登录成功：进入MainAct，否则提升进入登录页）
     */
    private void toLogin(Person person) {

        person.login(new SaveListener<Person>() {
            @Override
            public void done(Person person, BmobException e) {
                if (e == null) {
                    ThreadTool.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            ToActivityPage.turnToSimpleAct(RegisterAct.this, MainActivity.class);//进入主页
                        }
                    });
                } else {
                    final LoginAct loginAct = new LoginAct();
                    loginAct.setUsernameTagStr(person.getUsername());

                    ThreadTool.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToActivityPage.turnToTargetAct(RegisterAct.this, loginAct);
                        }
                    });
                }
            }
        });

    }
}
