package com.qiao_techcomponent;

public class QiaoImageLoad {

    private static QiaoImageLoad instance;

    public static synchronized QiaoImageLoad getInstance () {

        if (null == instance) {
            instance = new QiaoImageLoad();
        }
        return instance;
    }




}
