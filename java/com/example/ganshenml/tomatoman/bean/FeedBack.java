package com.example.ganshenml.tomatoman.bean;

import cn.bmob.v3.BmobObject;

/**
 * 用户的反馈
 * Created by ganshenml on 2016-07-22.
 */
public class FeedBack extends BmobObject {
    private String username;
    private String feedbackContent;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }
}
