package com.qiao_basemodule.hooklogin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/***
 * aop切面思想封装集中式登录框架 核心代码
 * 二次hook
 * 第一次hook到开启activity前的过程
 * 第二次hook到发送载入activity的msg的点
 */

public class HookUtil {

    private Context mContext;



    public void activityThreadHandlerHook (Context context) {

        this.mContext = context;

        try {
            Class<?> activityThreadClazz = Class.forName("android.app.ActivityThread");
            Field currentActivityThreadField = activityThreadClazz.getField("sCurrentActivityThread");
            currentActivityThreadField.setAccessible(true);
            //获取到ActivityThread引用对象
            Object activityThreadInstance = currentActivityThreadField.get(null);
            Field mHandlerField = activityThreadClazz.getDeclaredField("mH");
            mHandlerField.setAccessible(true);
            Handler mH = (Handler) mHandlerField.get(activityThreadInstance);
            Field callbackField = Handler.class.getDeclaredField("mCallback");
            callbackField.setAccessible(true);
            callbackField.set(mH,new MHCallback(mH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class MHCallback implements Handler.Callback {


        private Handler mH;

        public MHCallback(Handler mH) {
            this.mH = mH;
        }

        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 100) {

                //即将要加载一个Activity
                handleActivity (msg);
            }

            //继续交给系统，进行跳转
            mH.handleMessage(msg);
            return true;
        }
    }

    private void handleActivity(Message msg) {


        Object obj = msg.obj;
        try {
            Field intentField  = obj.getClass().getDeclaredField("intent");
            intentField.setAccessible(true);
            //还原真实要跳转的目标页面
            Intent realyIntent = (Intent) intentField.get(obj);
            //被隐藏的intent
            Intent oldIntent = realyIntent.getParcelableExtra("oldIntent");
            if (null != oldIntent) {
                //在这里做集中式业务逻辑处理，比如集中式登录
                //这里以登录为例来做
                boolean isLogin = false;

                if (isLogin) {

                    //如果登录成功,就按照原有意图进行跳转
                    realyIntent.setComponent(oldIntent.getComponent());
                } else {
                    ComponentName componentName = new ComponentName(mContext,LoginActivity.class);
                    //这里为了在登录页面登录成功后再跳转到原来要跳转的目标页面 oldIntent
                    realyIntent.putExtra("extraIntent",oldIntent.getComponent().getClassName());
                    realyIntent.setComponent(componentName);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void startActvityHook (Context context) {
        this.mContext = context;
        //还原gDefault对象 ActivityManagerNative类里
        try {
            Class<?> activityManagerNativeClazz = Class.forName("android.app.ActivityManagerNative");
            Field gDefault = activityManagerNativeClazz.getDeclaredField("gDefault");
            gDefault.setAccessible(true);
            Object gDefaultObj = gDefault.get(null);
            //mInstance对象
            Class<?> singletonClazz = Class.forName("android.util.singleton");
            Field mInstance = singletonClazz.getDeclaredField("mInstance");
            mInstance.setAccessible(true);
            //拿到IActivityManager
            Object iActivityManagerInstance = mInstance.get(gDefaultObj);

            //动态代理，代理IActivityManager对象

            Class<?> iActivityManagerClazz = Class.forName("android.app.IActivityManager");

            StartActivity startActivity = new StartActivity(iActivityManagerInstance);

            //生成的代理对象要实现iActivityManangerClazz接口
            Object oldIActivityManangerInstance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class[] {iActivityManagerClazz},
                    startActivity);
                    //替换系统的IActivityManager
            mInstance.set(gDefaultObj,oldIActivityManangerInstance);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * 动态代理最终要执行的方法是invoke这个
     * 在这里做我们想做的事情
     */
    class StartActivity implements InvocationHandler {

        private Object iActivityManagerInstance;

        public StartActivity(Object iActivityManagerInstance) {
            this.iActivityManagerInstance = iActivityManagerInstance;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            //动态代理，将系统的startActivity方法拿过来，在这里执行我们自己的逻辑，
            // 这里通过重新赋予新的Intent对象，完成Activity的跳转
            if ("startActivity".equals(method.getName())) {
                Intent intent = null;
                int index = 0;
                //startActivity方法是个重载函数，去遍历参数
                for (int i=0;i<args.length;i++) {
                    Object argObj = args[i];
                    if (argObj instanceof Intent) {
                        intent = (Intent) argObj;
                        index = i;
                    }
                }
                Intent newIntent = new Intent();
                ComponentName componentName = new ComponentName(mContext,ProxyActivity.class);
                newIntent.setComponent(componentName);
                //将真实的intent对象放到新的intent对象的键值对中
                newIntent.putExtra("oldIntent",intent);
                //重新生成一个Intent对象赋予系统传递的intent对象
                args[index] = newIntent;
            }

            return method.invoke(iActivityManagerInstance,args);
        }
    }
}
