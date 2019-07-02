package com.qiaobateach.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.qiao_techcomponent.pluginload.PluginLoadManager;
import com.qiaobateach.R;

public class BroadCastMainActivity extends AppCompatActivity{

    private static final String ACTION = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_apk_layout);
        PluginLoadManager.getInstance().setContext(this);
        registerReceiver(mReceiver,new IntentFilter(ACTION));
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"我是宿主，收到插件广播",Toast.LENGTH_LONG).show();
        }
    };
}
