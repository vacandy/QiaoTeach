package com.qiao_techcomponent.qiaohttp;

/***
 * 请求封装类
 */
public interface IHttpService {

    /**设置请求地址*/
    void setUrl (String url);
    /**设置请求参数*/
    void setReq (byte[] req);
    /**执行请求*/
    void execute ();

    /**设置回掉监听*/
    void setCallBack (IHttpListener l) ;
}
