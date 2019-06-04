package com.qiao_stander;

import android.app.Activity;
import android.os.Bundle;

/***
 * 插件接入宿主接口标准 插件需要实现该接口，以获取插件Activity的生命周期
 */
public interface QiaoInterface {

      void onCreate (Bundle onBundle);
      void onStart ();
      void onPause ();
      void onStop ();
      void onResume ();
      void onDestroy ();

     /***
      * 宿主注入上下文
      * @param context
      */
      void attach (Activity context);

}
