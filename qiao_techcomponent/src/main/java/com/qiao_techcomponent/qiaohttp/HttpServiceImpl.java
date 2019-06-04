package com.qiao_techcomponent.qiaohttp;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/***
 * 请求服务实现类
 */
public class HttpServiceImpl implements IHttpService{

    private String mUrl;
    private byte[] mReq;
    private IHttpListener mListener;
    private HttpURLConnection mUrlConnection = null;
    private URL mURL = null;

    @Override
    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public void setReq(byte[] req) {
        mReq = req;
    }


    /***
     * 执行网络请求
     */
    @Override
    public void execute() {
        httpPostRequest();
    }

    private void httpPostRequest () {
        try {
            if (null == mURL) {
                mURL = new URL(mUrl);
            }
            mUrlConnection = (HttpURLConnection) mURL.openConnection();
            mUrlConnection.setConnectTimeout(QiaoHttpConst.CONNECT_TIME_OUT);//连接超时时间
            mUrlConnection.setUseCaches(false);//不使用缓存
            mUrlConnection.setInstanceFollowRedirects(true);//是成员函数
            mUrlConnection.setReadTimeout(QiaoHttpConst.READ_TIME_OUT);//响应超时时间
            mUrlConnection.setDoInput(true);//是否可以写入数据
            mUrlConnection.setDoOutput(true);//是否可以输出数据
            mUrlConnection.setRequestMethod(QiaoHttpConst.REQUEST_METHOD_POST);//设置请求方式
            mUrlConnection.setRequestProperty("Content-Type","application/json;charset=utf-8");
            mUrlConnection.connect();
            //发送数据
            OutputStream os = mUrlConnection.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            if (null != mReq) {
                bos.write(mReq);
            }
            bos.flush();//刷新缓冲区
            os.close();
            bos.close();
            if (mUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //写入数据
                InputStream is = mUrlConnection.getInputStream();
                mListener.onSuccess(is);
            } else {
                mListener.onFaile();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mListener.onFaile();
        } finally {
            //断开连接
            mUrlConnection.disconnect();
        }

    }

    @Override
    public void setCallBack(IHttpListener l) {
        mListener = l;
    }
}
