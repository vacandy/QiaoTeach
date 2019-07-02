package com.qiao_techcomponent.qiao_eventbus;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import com.qiao_techcomponent.qiao_eventbus.service.QiaoService;
import java.util.concurrent.ConcurrentHashMap;



public class ServiceConnectionManager {
    private static final ServiceConnectionManager ourInstance = new ServiceConnectionManager();
    //    Class  对应的Binder  对象
    private final ConcurrentHashMap<Class<? extends QiaoService>, QiaoEventBusAidl> mHermesServices = new ConcurrentHashMap<Class<? extends QiaoService>, QiaoEventBusAidl>();

    public static ServiceConnectionManager getInstance() {
        return ourInstance;
    }
    //Class对应的链接对象
    private final ConcurrentHashMap<Class<? extends QiaoService>, HermesServiceConnection> mHermesServiceConnections = new ConcurrentHashMap<Class<? extends QiaoService>, HermesServiceConnection>();


    private ServiceConnectionManager() {
    }
    public void bind(Context context, String packageName, Class<? extends QiaoService> service) {
        HermesServiceConnection connection = new HermesServiceConnection(service);
        mHermesServiceConnections.put(service, connection);
        Intent intent ;
        if (TextUtils.isEmpty(packageName)) {
            intent = new Intent(context, service);
        } else {
            intent = new Intent();
            intent.setClassName(packageName, service.getName());
        }
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public Response request(Class<QiaoService> qiaoServiceClass, Request request) {
        QiaoEventBusAidl eventBusService = mHermesServices.get(qiaoServiceClass);
        if (eventBusService != null) {
            try {
                Response responce = eventBusService.send(request);
                return responce;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    //     接受远端的binder 对象   进程B就可以了通过Binder对象去操作 服务端的 方法
    private class HermesServiceConnection implements ServiceConnection {
        private Class<? extends QiaoService> mClass;

        HermesServiceConnection(Class<? extends QiaoService> service) {
            mClass = service;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            QiaoEventBusAidl hermesService = QiaoEventBusAidl.Stub.asInterface(service);
            mHermesServices.put(mClass, hermesService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mHermesServices.remove(mClass);
        }
    }

}
