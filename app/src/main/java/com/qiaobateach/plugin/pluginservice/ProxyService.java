package com.qiaobateach.plugin.pluginservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.qiao_stander.QiaoServiceInterface;
import com.qiao_techcomponent.pluginload.PluginLoadManager;

import java.lang.reflect.Constructor;

/***
 * 开启插件的Service桩
 * 将插件里要跳转的Service类加载进来，然后实例出一个对象
 * 通过接入标准方法，注入宿主的上下文，同时赋予插件Service的生命周期
 * 在这里赋予插件的生命周期
 */
public class ProxyService extends Service{

    private String mServiceName;
    private QiaoServiceInterface mServiceInterface;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        init(intent);
        return null;
    }

    private void init(Intent intent) {
        mServiceName = intent.getStringExtra("serviceName");
        try {
            //加载要开启的service服务类
            Class serviceClazz = PluginLoadManager.getInstance().getmDexClassLoader().loadClass(mServiceName);
            //获取service服务对象
            Constructor<?> constructor = serviceClazz.getConstructor(new Class[]{});
            Object instance = constructor.newInstance(new Object[]{});
            mServiceInterface = (QiaoServiceInterface) instance;
            //传递上下文到插件
            mServiceInterface.attach(this);
            //传递生命周期
            mServiceInterface.onCreate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == mServiceInterface) {
            init(intent);
        }
        mServiceInterface.onStartCommand(intent,flags,startId);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        mServiceInterface.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mServiceInterface.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mServiceInterface.onUnbind(intent);
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        mServiceInterface.onRebind(intent);
        super.onRebind(intent);
    }
}
