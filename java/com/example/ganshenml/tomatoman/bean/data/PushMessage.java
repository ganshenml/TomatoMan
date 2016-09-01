package com.example.ganshenml.tomatoman.bean.data;

import cn.bmob.v3.BmobObject;

/**
 * 推送的消息内容
 * Created by ganshenml on 2016-09-01.
 */
public class PushMessage extends BmobObject {
    private String pushTime;//预发布时间：为null或者""表示理解发布
    private Boolean isUsing;//当前是否可用
    private String deadlineTime;//有效期：为null或者""表示没有截止时间
    private String content;//内容
    /**
     * 只有当当前时间大于预发布时间，且状态为true，在有效期内才能正常显示
     */


    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public Boolean getUsing() {
        return isUsing;
    }

    public void setUsing(Boolean using) {
        isUsing = using;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
