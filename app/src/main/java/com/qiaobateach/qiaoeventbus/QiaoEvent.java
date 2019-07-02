package com.qiaobateach.qiaoeventbus;

import android.content.Context;

import com.qiao_techcomponent.qiao_eventbus.service.QiaoService;

public class QiaoEvent {

    private static final QiaoEvent ourInstance = new QiaoEvent();

    private Context mContext;

    public static QiaoEvent getInstance() {
        return ourInstance;
    }

    private QiaoEvent() {

    }


    public void connect (Context context, Class<? extends QiaoService> service) {

        connectApp(context,null,service);
    }

    private void connectApp (Context context,String packageName,Class<? extends QiaoService> service) {

        init(context);
    }

    private void init (Context context) {
        mContext = context.getApplicationContext();
    }
}
