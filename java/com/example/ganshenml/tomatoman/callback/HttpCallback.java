package com.example.ganshenml.tomatoman.callback;

import java.util.List;

/**
 * Created by ganshenml on 2016-07-21.
 */
public abstract class HttpCallback<T> {

    /**
     * 处理完成 返回成功的处理结果
     * @param data
     */
    public void onSuccess(T data){}
    /**
     * 处理完成 返回成功的处理结果
     * @param data
     */
    public void onSuccess(T data,String resultStr){}

    /**
     * 处理完成 返回成功的处理结果集合
     * @param data
     */
    public void onSuccess(List<T> data){}

    /**
     * 一般处理动作的完成
     * @param data
     */
    public void onComplete(T data){}
    /**
     * 处理失败,一般是处理服务器的业务逻辑出错
     * @param msg
     */
    public void onFailure(int code,Object msg){}

}
