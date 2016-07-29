package com.example.ganshenml.tomatoman.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by ganshenml on 2016-07-28.
 */
public class TomatoRecord extends BmobObject {
    private Person person;//用户
    private String taskTime;//任务完成的时间
    private String week;//任务完成所属星期
    private String completeState;//完成状态：已完成、未完成
    private Integer tomatoNum = 0;//获得的番茄数
    private Integer tomatoTime = 0; //获得的番茄时间

    private String efficientTime;//高效时间：无或者以分钟计算
    private String tomatoNote;//备注
    private Integer evaluateLever = 0;//评价：1,2,3,4,5分（0分表示未评分，默认）


    public Integer getTomatoTime() {
        return tomatoTime;
    }

    public void setTomatoTime(Integer tomatoTime) {
        this.tomatoTime = tomatoTime;
    }



    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getCompleteState() {
        return completeState;
    }

    public void setCompleteState(String completeState) {
        this.completeState = completeState;
    }

    public Integer getTomatoNum() {
        return tomatoNum;
    }

    public void setTomatoNum(Integer tomatoNum) {
        this.tomatoNum = tomatoNum;
    }

    public String getEfficientTime() {
        return efficientTime;
    }

    public void setEfficientTime(String efficientTime) {
        this.efficientTime = efficientTime;
    }

    public String getTomatoNote() {
        return tomatoNote;
    }

    public void setTomatoNote(String tomatoNote) {
        this.tomatoNote = tomatoNote;
    }

    public Integer getEvaluateLever() {
        return evaluateLever;
    }

    public void setEvaluateLever(Integer evaluateLever) {
        this.evaluateLever = evaluateLever;
    }
}
