package com.example.ganshenml.tomatoman.tool;

import android.content.Context;

import com.example.ganshenml.tomatoman.act.BaseActivity;
import com.example.ganshenml.tomatoman.bean.ContextBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganshenml on 2016/4/23.
 * 对context(主要是activity)进行管理的工具类：1.进入一个activity即加入静态List，activity销毁时，从list中移除
 */
public class ContextManager {
    public static List<ContextBean> list = new ArrayList<>();

    //1.添加一个activity进入list，并设置其标志位为正在使用中，且遍历目前整个list，将其他activity的标志位设置为没在使用(false)
    public static void addContext(Context context){
        //1.先将该context添加进list
        ContextBean contextBean = new ContextBean(context);
        list.add(contextBean);

        //2.将list列表中的其他context的标志位（使用状态）设置为没在使用
        int size = list.size()-1;
        for (int i = 0; i < size; i++) {
            list.get(i).setUsing(false);
        }
    }

    //2.从list中移除该activity
    public static void removeContext(Context context){
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if(list.get(i).getContext() == context){//如果是同一个context，则移除当前
                list.remove(i);
                return;
            }
        }
    }

    public static void finishAndRemoveAllContext(){//仅finish掉所有加入该静态list的context
        for (int i = 0; i < list.size(); i++) {
            BaseActivity activity = (BaseActivity)list.get(i).getContext();
            list.remove(i);
            activity.finish();
        }
    }

    //3.得到当前正在前台的activity
    public static Context getCurrentRunningContext(){
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if(list.get(i).isUsing() == true){
                return list.get(i).getContext();
            }
        }
        return  null;
    }
}
