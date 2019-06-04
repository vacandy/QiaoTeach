package com.qiao_techcomponent.qiaohttp;


import com.alibaba.fastjson.JSON;


public class HttpTask<T> implements Runnable{

    private IHttpService httpService;
    private IHttpListener mHttpListener;

    public HttpTask(T req,String url,IHttpService httpService,IHttpListener httpListener) {

        this.httpService = httpService;
        this.mHttpListener = httpListener;
        httpService.setUrl(url);
        httpService.setCallBack(mHttpListener);
        if (null != req) {
            String reqStr = JSON.toJSONString(req);
            try {
                httpService.setReq(reqStr.getBytes("utf-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void run() {
        httpService.execute();
    }
}
