package com.qiao_techcomponent.qiao_eventbus;

import android.text.TextUtils;


import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class TypeCenter {
    private static final TypeCenter ourInstance = new TypeCenter();

    public static TypeCenter getInstance() {
        return ourInstance;
    }

    //name   String
    private final ConcurrentHashMap<String, Class<?>> mAnnotatedClasses;
    //类对应的方法 app --class  --->n   method String  name
    private final ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>> mRawMethods;

    private TypeCenter() {
        mAnnotatedClasses = new ConcurrentHashMap<String, Class<?>>();
        mRawMethods = new ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>>();
    }
    public void register(Class<?> clazz) {
//分为注册类  注册方法
        registerClass(clazz);
        registerMethod(clazz);

    }


    public Class<?> getClassType(String name)   {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        Class<?> clazz = mAnnotatedClasses.get(name);
        if (clazz == null) {
            try {
                clazz = Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }
    //mAnnotatedMethods 填充
    private void registerMethod(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            mRawMethods.putIfAbsent(clazz, new ConcurrentHashMap<String, Method>());
//            map都不会为空
            ConcurrentHashMap<String, Method> map = mRawMethods.get(clazz);
            String key = TypeUtils.getMethodId(method);
            map.put(key, method);
        }
    }
    //    mAnnotatedClasses 填充
    private void registerClass(Class<?> clazz) {
        String className = clazz.getName();
        mAnnotatedClasses.putIfAbsent(className, clazz);
    }

}
