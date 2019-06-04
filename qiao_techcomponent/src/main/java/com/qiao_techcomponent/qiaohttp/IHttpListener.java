package com.qiao_techcomponent.qiaohttp;

import java.io.InputStream;

/***
 * 响应
 */
public interface IHttpListener {

    /***接收上一个接口响应结果
     * @param is
     */
    void onSuccess (InputStream is);
    void onFaile ();
}
