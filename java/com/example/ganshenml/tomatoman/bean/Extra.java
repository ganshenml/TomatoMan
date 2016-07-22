package com.example.ganshenml.tomatoman.bean;

import cn.bmob.v3.BmobObject;

/**
 * 存放APP一些其他的数据：版本号
 * Created by ganshenml on 2016-07-22.
 */
public class Extra extends BmobObject {
    public String getFeedbackHint() {
        return feedbackHint;
    }

    public void setFeedbackHint(String feedbackHint) {
        this.feedbackHint = feedbackHint;
    }

    private String appVersion;//app版本号
    private String feedbackHint;//反馈提示



    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
