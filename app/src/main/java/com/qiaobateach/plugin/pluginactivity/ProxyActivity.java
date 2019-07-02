package com.qiaobateach.plugin.pluginactivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qiao_stander.QiaoInterface;
import com.qiao_techcomponent.pluginload.PluginLoadManager;
import com.qiaobateach.plugin.pluginbroadcast.ProxyBroadCast;
import com.qiaobateach.plugin.pluginservice.ProxyService;

import java.lang.reflect.Constructor;

/***
 * 开启插件的Activity桩
 * 将插件里要跳转的Activity类加载进来，然后实例出一个对象
 * 通过接入标准方法，注入宿主的上下文，同时赋予插件Activity的生命周期
 * 在这里赋予插件的生命周期
 */
public class ProxyActivity extends Activity{


    private String className;
    private QiaoInterface mQiaoInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        className = getIntent().getStringExtra("className");

        try {
            Class activity = getClassLoader().loadClass(className);
            Constructor constructor = activity.getConstructor(new Class[]{});
            Object instance = constructor.newInstance(new Object[]{});
            mQiaoInterface = (QiaoInterface)instance;
            mQiaoInterface.attach(this);
            Bundle bundle = new Bundle();
            mQiaoInterface.onCreate(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQiaoInterface.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mQiaoInterface.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mQiaoInterface.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mQiaoInterface.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mQiaoInterface.onStop();
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginLoadManager.getInstance().getmDexClassLoader();
    }

    @Override
    public Resources getResources() {
        return PluginLoadManager.getInstance().getmResources();
    }

    /***
     * 启动服务
     * @param service
     * @return
     */
    @Override
    public ComponentName startService(Intent service) {
        String serviceName = service.getStringExtra("serviceName");
        Intent intent = new Intent(this, ProxyService.class);
        intent.putExtra("serviceName",serviceName);
        return super.startService(intent);

    }


    /**
     * 插件广播注册
     * @param receiver
     * @param filter
     * @return
     */
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        IntentFilter intentFilter = new IntentFilter();
        for (int i=0;i<filter.countActions();i++) {
            intentFilter.addAction(filter.getAction(i));
        }
        return super.registerReceiver(new ProxyBroadCast(receiver.getClass().getName(),this), intentFilter);
    }

    @Override
    public void startActivity(Intent intent) {
        String className = intent.getStringExtra("className");
        /***
         * 跳转自己，由代理Activity跳转到目标页面，为了重新赋予插件要跳转的activity的生命周期
         */
        Intent i = new Intent(this, ProxyActivity.class);
        i.putExtra("className",className);
        super.startActivity(i);
    }
}
