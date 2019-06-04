package com.qiaobateach.qiaostander;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qiao_stander.QiaoInterface;

import java.lang.reflect.Constructor;

public class LoadPluginActivity extends Activity{


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


    @Override
    public void startActivity(Intent intent) {
        String className = intent.getStringExtra("className");
        /***
         * 跳转自己，为了重新赋予插件要跳转的activity的生命周期
         */
        Intent i = new Intent(this,LoadPluginActivity.class);
        i.putExtra("className",className);
        super.startActivity(i);
    }
}
