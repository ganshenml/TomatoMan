package com.example.ganshenml.tomatoman.fragment;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.act.TomatoCountTimeAct;
import com.example.ganshenml.tomatoman.util.ConstantCode;
import com.example.ganshenml.tomatoman.util.NotificationUtls;
import com.example.ganshenml.tomatoman.view.ClearEditTextView;
import com.example.ganshenml.tomatoman.view.StartCountTimeCircleView;

public class HomeFragment extends Fragment {

    View view;
    private StartCountTimeCircleView startCountTimeCircleViewId;
    private ClearEditTextView etTaskName;
    private LinearLayout llHomeFragment;
    private static final int REQUESTCODE_TO_TOMATOCOMPLETE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews();
        listenerMethod();
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //-------------------------------------------------------以下为自定义方法----------------------------------------------
    private void initViews() {
        startCountTimeCircleViewId = (StartCountTimeCircleView) view.findViewById(R.id.startViewId);
        etTaskName = (ClearEditTextView) view.findViewById(R.id.etTaskName);
        llHomeFragment = (LinearLayout) view.findViewById(R.id.llHomeFragment);
    }

    private void listenerMethod() {
        startCountTimeCircleViewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//1.样式变换；2.notification开启；3.finish当前activity；4.跳转至下一个activity并传递数据
                //1.样式：设置背景alpha变化以下
                v.setAlpha(0.5f);

                //2.notification开启
                Intent intent = new Intent(getActivity(), TomatoCountTimeAct.class);
                NotificationUtls.sendNotification(getActivity(), ConstantCode.HOMEFRAGMETN_REQUEST_CODE, intent, R.layout.notification_layout);//调用自定义工具类方法发送notification

                //3.finish当前主activity
                getActivity().finish();

                //4.逻辑：跳转至下一个Activity
                startActivity(intent);
            }
        });

        //判断软键盘是否打开——>如果打开，则在点击界面后进行隐藏
        llHomeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 101) {//表示返回首页，那么所有状态应该恢复原状
//            startViewId.setAlpha(1.0f);
//            etTaskName.setText("");
//        }
    }

}
