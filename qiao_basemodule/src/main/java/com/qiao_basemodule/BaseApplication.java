package com.qiao_basemodule;

import android.app.Application;

import com.qiao_basemodule.hooklogin.HookUtil;

public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        HookUtil hookUtil = new HookUtil();
        hookUtil.startActvityHook(getApplicationContext());
        hookUtil.activityThreadHandlerHook(getApplicationContext());
    }
}
