package com.example.ganshenml.tomatoman.callback;

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
     * 处理失败,一般是处理服务器的业务逻辑出错
     * @param msg
     */
    public void onFailure(int code,Object msg){}

}
