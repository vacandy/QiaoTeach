package com.qiao_techcomponent.qiao_eventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QiaoEventBus {


    private static final QiaoEventBus ourInstance = new QiaoEventBus();

    private ExecutorService mExecutorService;
    //总表
    private Map<Object, List<SubscribleMethod>> mCacheMap;

    private Handler mHandler;

    public static QiaoEventBus getInstance() {
        return ourInstance;
    }

    private QiaoEventBus() {
        this.mCacheMap = new HashMap<>();
        mExecutorService = Executors.newCachedThreadPool();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void register(Object o) {


        List<SubscribleMethod> list = mCacheMap.get(o);
       //如果已经注册  就不需要注册
        if (list == null) {
            list = getSubscribleMethods(o);
            mCacheMap.put(o, list);
        }

    }

    public void unRegister (Object o) {
        if (null != mCacheMap && mCacheMap.containsKey(o)) {
            mCacheMap.remove(o);
        }
    }

    //寻找能够接受事件的方法
    private List<SubscribleMethod> getSubscribleMethods(Object activity) {
        List<SubscribleMethod> list = new ArrayList<>();

        Class clazz = activity.getClass();

        //需要  Object  BaseActivity   ---->Activity(找 )
        while (clazz != null) {

            String name = clazz.getName();
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                break;
            }
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if (subscribe == null) {
                    continue;
                }
               // 监测这个方法 合不合格
                Class[] paratems = method.getParameterTypes();
                if (paratems.length != 1) {
                    throw new RuntimeException("只能接收到一个参数");
                }
               // 符合要求
                ThreadMode threadMode = subscribe.threadMode();
                SubscribleMethod subscribleMethod = new SubscribleMethod(method
                        , threadMode, paratems[0]);
                list.add(subscribleMethod);

            }
            clazz = clazz.getSuperclass();
        }

        return list;

    }


    /***
     * 发送消息
     * @param o 消息体
     */

    public void post(final Object o) {

        //遍历 找到
        Set<Object> set = mCacheMap.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            final Object activity = iterator.next();
            List<SubscribleMethod> list = mCacheMap.get(activity);
            for (final SubscribleMethod subscribleMethod : list) {
                //判断 这个方法是否应该接受事件
                if (subscribleMethod.getEventType().isAssignableFrom(o.getClass())) {

                    switch (subscribleMethod.getThreadMode()) {
                        //接受方法在子线程执行的情况
                        case Async:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                //post方法  执行在主线程
                                mExecutorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, activity, o);
                                    }
                                });

                            } else {
                               // post方法  执行在子线程
                                invoke(subscribleMethod, activity, o);
                            }
                            break;
                        //                        接受方法在主线程执行的情况
                        case MainThread:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                //需要  1  不需要2
                                invoke(subscribleMethod, activity, o);
                            } else {
                              //post方法  执行在子线程     接受消息 在主线程
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, activity, o);
                                    }
                                });
                            }
                            break;
                        case PostThread:
                    }

                }

            }

        }

    }


    /***
     * 调用需要接收信息的方法
     * @param subscribleMethod
     * @param viewO
     * @param o
     */
    private void invoke(SubscribleMethod subscribleMethod, Object viewO, Object o) {
        Method method = subscribleMethod.getMethod();
        try {
            method.invoke(viewO, o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
