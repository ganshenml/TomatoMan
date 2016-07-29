package com.example.ganshenml.tomatoman.bean.beant;

import com.example.ganshenml.tomatoman.bean.Extra;

import org.litepal.crud.DataSupport;

/**
 * 与Extra对应的类，用来做LitePal的本地存储
 * Created by ganshenml on 2016-07-29.
 */
public class ExtraT extends DataSupport {

    private String appVersion;//app版本号
    private String feedbackHint;//反馈提示

    public ExtraT(Extra extra){
        this.appVersion = extra.getAppVersion();
        this.feedbackHint = extra.getFeedbackHint();
    }

    public String getFeedbackHint() {
        return feedbackHint;
    }

    public void setFeedbackHint(String feedbackHint) {
        this.feedbackHint = feedbackHint;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

}
