package com.qiaobachild;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.qiao_stander.QiaoInterface;

public class ChildBaseActivity extends Activity implements QiaoInterface{


    protected Activity that;

    @Override
    public void setContentView(int layoutResID) {
        if (null == that) {
            super.setContentView(layoutResID);
        } else {
            that.setContentView(layoutResID);
        }

    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (null == that) {
            return super.findViewById(id);
        } else {
            return that.findViewById(id);
        }
    }


    @Override
    public ClassLoader getClassLoader() {
        if (null == that) {
            return super.getClassLoader();
        } else {
            return that.getClassLoader();
        }
    }


    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        if (null == that) {
            return super.getLayoutInflater();
        } else {
            return that.getLayoutInflater();
        }
    }


    @Override
    public Window getWindow() {
        if (null == that) {
            return super.getWindow();
        } else {
            return that.getWindow();
        }
    }


    @Override
    public WindowManager getWindowManager() {
        if (null == that) {
            return super.getWindowManager();
        } else {
            return that.getWindowManager();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle onBundle) {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onPause() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStop() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {

    }

    /**
     * 注入上下文
     * @param context
     */
    @Override
    public void attach(Activity context) {
        that = context;
    }

    @Override
    public void startActivity(Intent intent) {
        Intent i = new Intent();
        i.putExtra("className",intent.getComponent().getClassName());
        Log.e("intent.getComponent",intent.getComponent().getClassName());
        that.startActivity(i);
    }
}
