package com.qiao_stander;

import android.content.Context;
import android.content.Intent;

/**
 * 插件广播标准
 */
public interface QiaoBroadCast {

    void attach (Context context);

    void onReceive (Context context, Intent intent);

}
