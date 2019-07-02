package com.qiao_techcomponent.qiao_eventbus.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.qiao_techcomponent.qiao_eventbus.QiaoEventBusAidl;
import com.qiao_techcomponent.qiao_eventbus.Request;
import com.qiao_techcomponent.qiao_eventbus.Response;

public class QiaoService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    private QiaoEventBusAidl.Stub mBinder = new QiaoEventBusAidl.Stub() {
        @Override
        public Response send(Request request) throws RemoteException {
            return null;
        }
    };

}
