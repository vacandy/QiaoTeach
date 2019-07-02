package com.qiaobateach.qiaoeventbus;

public class ServiceConnectManager {

    private static final ServiceConnectManager ourInstance = new ServiceConnectManager();

    public static ServiceConnectManager getInstance() {
        return ourInstance;
    }

    private ServiceConnectManager() {

    }
}
