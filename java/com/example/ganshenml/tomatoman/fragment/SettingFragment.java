package com.example.ganshenml.tomatoman.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.act.LoginAct;
import com.example.ganshenml.tomatoman.bean.Extra;
import com.example.ganshenml.tomatoman.bean.FeedBack;
import com.example.ganshenml.tomatoman.callback.HttpCallback;
import com.example.ganshenml.tomatoman.util.CommonUtils;
import com.example.ganshenml.tomatoman.util.DbTool;
import com.example.ganshenml.tomatoman.util.LogTool;
import com.example.ganshenml.tomatoman.util.ShowDialogUtils;
import com.example.ganshenml.tomatoman.util.ShowDrawerLayout;
import com.example.ganshenml.tomatoman.util.ToActivityPage;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by ganshenml on 2016/3/31.
 */
public class SettingFragment extends Fragment {
    private LinearLayout logoutLl;
    private TextView appVersionTv;
    private EditText feedbackEt;
    private Button feedbackBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initDataViews();
        initListeners();
    }

    private void initViews() {
        View viewTemp = getView();
        appVersionTv = (TextView) viewTemp.findViewById(R.id.appVersionTv);
        logoutLl = (LinearLayout) viewTemp.findViewById(R.id.logoutLl);
        feedbackEt = (EditText) viewTemp.findViewById(R.id.feedbackEt);
        feedbackBtn = (Button) viewTemp.findViewById(R.id.feedbackBtn);
    }

    private void initDataViews() {
        Extra extraTemp = DbTool.findLocalExtraData();//返回本地存储的Extra数据
        if (extraTemp != null) {
            feedbackEt.setHint(extraTemp.getFeedbackHint());
        }
        appVersionTv.setText(getResources().getText(R.string.app_version) + " " + CommonUtils.getCurrentAppVersion(getActivity()));


    }

    private void initListeners() {
        //为左侧汉堡菜单设置点击监听事件：显示DrawerLayout
        ShowDrawerLayout.showDrawerLayout(getActivity(), R.id.tbSetting, "设置", R.id.drawer_layout);

        logoutLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注销账号弹窗
                ShowDialogUtils.showSimpleHintDialog(getActivity(), "确定退出该账号？", new HttpCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        BmobUser.logOut();
                        getActivity().finish();
                        ToActivityPage.turnToSimpleAct(getActivity(), LoginAct.class);
                    }
                });
            }
        });

        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedbackStrTemp = feedbackEt.getText().toString();
                if (feedbackStrTemp.length() > 0) {
                    FeedBack feedBack = new FeedBack();
                    feedBack.setUsername(BmobUser.getCurrentUser().getUsername());
                    feedBack.setFeedbackContent(feedbackStrTemp);
                    feedBack.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                Toast.makeText(getActivity(),"提交成功",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(),"提交失败",Toast.LENGTH_SHORT).show();
                                LogTool.log(LogTool.Aaron," settingFragment 提交反馈失败： "+e.toString());
                            }
                        }
                    });
                }
            }
        });
    }

}
