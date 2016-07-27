package com.example.ganshenml.tomatoman.bean.data;

/**
 * 存放静态数据
 * Created by ganshenml on 2016-07-23.
 */
public class StaticData {
    public static boolean isFromUserHomePageAct = false;

    public static final int REQUEST_TO_MYTOMATO_ACT = 1001;//跳转至“我的番茄”页面的请求码
    public static final int REQUEST_TO_MYFRIENDS_ACT = 1002;//跳转至“我的好友”页面的请求码
    public static final int REQUEST_TO_RANK_ACT = 1003;//跳转至“排行榜”页面的请求码
    public static final int REQUEST_TO_SETTING_ACT = 1004;//跳转至“设置”页面的请求码


    public static final int RESULT_FROM_MYTOMATO_ACT = 1001;//从“我的番茄”页面返回的结果码
    public static final int RESULT_FROM_MYFRIENDS_ACT = 1002;//从“我的好友”页面返回的结果码
    public static final int RESULT_FROM_RANK_ACT = 1003;//从“排行榜”页面返回的结果码
    public static final int RESULT_FROM_SETTING_ACT = 1004;//从“设置”页面返回的结果码
}
