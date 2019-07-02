package com.qiao_techcomponent.qiao_eventbus;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.qiao_techcomponent.qiao_eventbus.annotion.ClassId;
import com.qiao_techcomponent.qiao_eventbus.bean.RequestBean;
import com.qiao_techcomponent.qiao_eventbus.bean.RequestParameter;
import com.qiao_techcomponent.qiao_eventbus.service.QiaoService;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/5/21.
 */

public class QiaoProcess {
//得到对象
    public static final int TYPE_NEW = 0;
//得到单例
    public static final int TYPE_GET = 1;

    private static final QiaoProcess ourInstance = new QiaoProcess();
    private TypeCenter typeCenter;
    private    ServiceConnectionManager serviceConnectionManager ;
    public static QiaoProcess getDefault() {
        return ourInstance;
    }
    private Context mContext;
    private QiaoProcess() {
        serviceConnectionManager = ServiceConnectionManager.getInstance();
        typeCenter = TypeCenter.getInstance();
    }


    public void register(Class<?> clazz) {

        typeCenter.register(clazz);
    }

    public void connect(Context context, Class<? extends QiaoService> service) {
        connectApp(context, null, service);
    }
    public    void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public   void connectApp(Context context, String packageName, Class<? extends QiaoService> service) {
        init(context);
        serviceConnectionManager.bind(context.getApplicationContext(), packageName, service);

    }

    //主要防止方法重载
    public <T> T getInstance(Class<T> clazz, Object... parameters) {
        Response responce = sendRequest(QiaoService.class, clazz, null, parameters);
        return null;
    }

    private <T> Response sendRequest(Class<QiaoService> qiaoServiceClass
            , Class<T> clazz, Method method, Object[] parameters) {
        RequestBean requestBean = new RequestBean();

        String className = null;
        if (clazz.getAnnotation(ClassId.class) == null) {
//            当
            requestBean.setClassName(clazz.getName());
            requestBean.setResultClassName(clazz.getName());
        } else {
//            返回类型的全类名
            requestBean.setClassName(clazz.getAnnotation(ClassId.class).value());
            requestBean.setResultClassName(clazz.getAnnotation(ClassId.class).value());
        }
        if (method != null) {
//            方法名 统一   传   方法名+参数名  getInstance(java.lang.String)
            requestBean.setMethodName(TypeUtils.getMethodId(method));

        }
//
        RequestParameter[] requestParameters = null;
        if (parameters != null && parameters.length > 0) {
            requestParameters=new RequestParameter[parameters.length];
            for (int i=0;i<parameters.length ;i++) {
                Object parameter = parameters[i];
                String parameterClassName = parameter.getClass().getName();
                String parameterValue = JSON.toJSONString(parameter);

                RequestParameter requestParameter = new RequestParameter(parameterClassName, parameterValue);
                requestParameters[i]=requestParameter;
            }
        }

        if (requestParameters != null) {
            requestBean.setRequestParameter(requestParameters);
        }

//        请求获取单例 ----》对象 ----------》调用对象的方法
        Request request = new Request(JSON.toJSONString(requestBean), TYPE_GET);
        return serviceConnectionManager.request(qiaoServiceClass,request);


    }


}
