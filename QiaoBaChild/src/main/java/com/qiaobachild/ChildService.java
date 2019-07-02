package com.qiaobachild;


import com.qiaobachild.base.BaseService;

public class ChildService extends BaseService{

    @Override
    public void onCreate() {
        super.onCreate();
        /***
         * 开始搞事情
         */
        new Thread(new Runnable() {
            @Override
            public void run() {

                //....
            }
        }).start();

    }
}
