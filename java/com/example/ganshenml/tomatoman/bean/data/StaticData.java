package com.example.ganshenml.tomatoman.bean.data;

import com.example.ganshenml.tomatoman.tool.SpTool;

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

    public static final String SPTOMATOCOMPLETENUM = "spTomatoCompletedNum";//当前完成的番茄数量
    public static final String SPTOMATOCOMPLETEEFFICIENTTIME = "spTomatoCompletedEfficientTime";//当前完成的“高效时间”
    public static final String SPWORKTIME = "spWorkTime";//当前设置的每个番茄的时长
    public static final String SPSHORTRESTTIME = "spShortRestTime";
    public static final String SPLONGRESTTIME = "spLongRestTime";
    public static final String SPVIBRATEALARM = "spVibrateAlarm";
    public static final String SPRINGTONEALARM = "spRingtoneAlarm";
    public static final String SPRINGTONEALARMURI = "spRingtoneAlarmURI";//音频字符串

    public static final String SPTASKNAME = "spTaskName";//任务名称

    public static final String SPLATESTCREATEDAT = "spLatestCreatedAt";//本地存储的TomatoRecordT数据字段CreatedAt最大的数据（每次完成任务后保存后将该数据上传成功后返回并更新至sp）

    public static final String SPPUSHMESSAGE = "spPushMessage";//push的消息：value存储的是日期，当用户读取了新的消息后更新为新的消息创建的时间
}
