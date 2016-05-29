package com.example.ganshenml.tomatoman.bean;

import android.content.Context;

/**
 * Created by ganshenml on 2016/4/23.
 * 主要用来装载Activity及service对象及其标识（是否正在使用）
 */
public class ContextBean {
    private Context context;
    private boolean isUsing = true;

    public ContextBean(Context context) {
        this.context = context;
        this.isUsing = true;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isUsing() {
        return isUsing;
    }

    public void setUsing(boolean using) {
        isUsing = using;
    }
}
