package com.qiao_techcomponent.qiaohttp;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 数据流处理，返回到上层接口
 * @param <V> 调用层传入实体 泛型
 */
public class HttpListenerImpl<V> implements IHttpListener {

    private Class<V> responseClass = null;
    private DataCallBack<V> callBack;
    private Handler mHandler = new Handler(Looper.getMainLooper());


    public HttpListenerImpl (Class<V> responseClass,DataCallBack callBack) {
        this.responseClass = responseClass;
        this.callBack = callBack;
    }

    @Override
    public void onSuccess(InputStream is) {
        //返回数据
        String resStr = getResContent(is);
        final V response = JSON.parseObject(resStr,responseClass);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (null != callBack) {
                    callBack.onSuccess(response);
                }
            }
        });
    }

    /***
     * 将二进制流转成字符串
     * @param is
     * @return
     */
    private String getResContent (InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String str = null;
        try {
            while ((str = reader.readLine()) != null) {
                sb.append(str + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
       return sb.toString();
    }

    @Override
    public void onFaile() {
        if (null != callBack) {
            callBack.onFaile();
        }
    }
}
