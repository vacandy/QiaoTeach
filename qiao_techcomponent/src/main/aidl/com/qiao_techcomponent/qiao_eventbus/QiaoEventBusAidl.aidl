// QiaoEventBusAidl.aidl
package com.qiao_techcomponent.qiao_eventbus;
import com.qiao_techcomponent.qiao_eventbus.Request;
import com.qiao_techcomponent.qiao_eventbus.Response;

interface QiaoEventBusAidl{
    Response send(in Request request);
}





