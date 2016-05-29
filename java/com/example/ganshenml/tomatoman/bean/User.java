package com.example.ganshenml.tomatoman.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by ganshenml on 2016/4/1.
 */
public class User extends BmobObject {
    private Integer userId;
    private String userName;
    private String userIntroduction;
    private String password;
    private Integer userGender;
    private Integer userTomatoNum;
    private Integer imageId;
    private Float userTomatoTimeNum;

    //---------------------------------------------以下为getter与setter方法-----------------------


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIntroduction() {
        return userIntroduction;
    }

    public void setUserIntroduction(String userIntroduction) {
        this.userIntroduction = userIntroduction;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserGender() {
        return userGender;
    }

    public void setUserGender(Integer userGender) {
        this.userGender = userGender;
    }

    public Integer getUserTomatoNum() {
        return userTomatoNum;
    }

    public void setUserTomatoNum(Integer userTomatoNum) {
        this.userTomatoNum = userTomatoNum;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Float getUserTomatoTimeNum() {
        return userTomatoTimeNum;
    }

    public void setUserTomatoTimeNum(Float userTomatoTimeNum) {
        this.userTomatoTimeNum = userTomatoTimeNum;
    }
}