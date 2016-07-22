package com.example.ganshenml.tomatoman.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by ganshenml on 2016-04-30.
 */
public class Person extends BmobUser {
//    public String username;//继承自BmobUser后username和password不能继续定义了
//    public String password;
    public String address;
    public String introduction;
    private Integer gender;
    private Integer tomatoNum;
    private String imageId;
    private String tomatoTimeNum;
    private Integer isUsing;

//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getTomatoNum() {
        return tomatoNum;
    }

    public void setTomatoNum(Integer tomatoNum) {
        this.tomatoNum = tomatoNum;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getTomatoTimeNum() {
        return tomatoTimeNum;
    }

    public void setTomatoTimeNum(String tomatoTimeNum) {
        this.tomatoTimeNum = tomatoTimeNum;
    }

    public Integer getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(Integer isUsing) {
        this.isUsing = isUsing;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
