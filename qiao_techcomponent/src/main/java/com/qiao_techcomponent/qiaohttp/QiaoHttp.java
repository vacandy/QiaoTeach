package com.qiao_techcomponent.qiaohttp;

/***
 * 外部调用入口
 */
public class QiaoHttp {

    public static<T,V> void httpRequest (T req,String url,Class<V> res,DataCallBack<V> callBack) {
        IHttpService httpService = new HttpServiceImpl();
        IHttpListener httpListener = new HttpListenerImpl(res,callBack);
        HttpTask<T> httpTask = new HttpTask<>(req,url,httpService,httpListener);
        TaskPoolManager.getInstance().execute(httpTask);
    }

}
