package com.example.ganshenml.tomatoman.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by ganshenml on 2016-04-30.
 */
public class Person extends BmobObject {
    public String name ;
    public String address;
    public String introduction;
    private String password;
    private Integer gender;
    private Integer tomatoNum;
    private String imageId;
    private String tomatoTimeNum;
    private Integer isUsing;


    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
