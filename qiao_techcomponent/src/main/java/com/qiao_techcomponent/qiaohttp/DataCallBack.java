package com.qiao_techcomponent.qiaohttp;

/***
 * 将结果回调到调用层
 */
public interface DataCallBack<T> {

    void onFaile ();

    void onSuccess (T t);

}
