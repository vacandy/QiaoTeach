package com.qiao_techcomponent.qiaohttp;

/***
 * 外部调用入口
 * 这里目前只实现了post请求，其他请求如get put delete等可以自行封装进来，略作改动即可实现
 */
public class QiaoHttp {

    public static<T,V> void httpRequest (T req,String url,Class<V> res,DataCallBack<V> callBack) {
        IHttpService httpService = new HttpServiceImpl();
        IHttpListener httpListener = new HttpListenerImpl(res,callBack);
        HttpTask<T> httpTask = new HttpTask<>(req,url,httpService,httpListener);
        TaskPoolManager.getInstance().execute(httpTask);
    }

}
