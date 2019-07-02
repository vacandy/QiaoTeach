package com.qiaobachild;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class StaticBoradCast extends BroadcastReceiver{

    private static final String ACTION = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context,"我是插件，收到宿主广播通知",Toast.LENGTH_LONG).show();
        context.sendBroadcast(new Intent(ACTION));
    }
}
