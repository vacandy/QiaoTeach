package com.qiaobateach.plugin.pluginbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.qiao_stander.QiaoBroadCast;
import com.qiao_techcomponent.pluginload.PluginLoadManager;

import java.lang.reflect.Constructor;


public class ProxyBroadCast extends BroadcastReceiver{

    private String mClassName;
    private QiaoBroadCast mBroadCast;

    public ProxyBroadCast (String className,Context context) {
        this.mClassName = className;
        try {
            Class clazz = PluginLoadManager.getInstance().getmDexClassLoader().loadClass(mClassName);
            Constructor<?> constructor = clazz.getConstructor(new Class[]{});
            Object instance = constructor.newInstance(new Object[] {});
            mBroadCast = (QiaoBroadCast)instance;
            mBroadCast.attach(context);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mBroadCast.onReceive(context,intent);
    }
}
