package com.example.ganshenml.tomatoman.bean;

import android.provider.ContactsContract;

import cn.bmob.v3.BmobObject;

/**
 * 用户的反馈
 * Created by ganshenml on 2016-07-22.
 */
public class FeedBack extends BmobObject {
    private Person person;
    private String feedbackContent;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }
}
