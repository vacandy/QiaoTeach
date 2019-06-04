package com.qiaobateach.mvp;

import com.qiao_basemodule.mvp.BasePresent;
import com.qiao_basemodule.mvp.Data;
import com.qiao_basemodule.mvp.IBaseModel;

import java.util.List;

public class DemoPresent<T extends IDemoBaseView> extends BasePresent<T>{

    //model层引用
    private IBaseModel iBaseModel = new DemoModelImpl();



    public DemoPresent() {

    }


    //获取数据
    public void fetchData () {
        if (null != mViewRef.get()) {
            mViewRef.get().showLoading();
            if (null != iBaseModel) {
                iBaseModel.loadData(new IBaseModel.DataLoadListener() {
                    @Override
                    public void onComplete(List<Data> data) {
                        if (null != mViewRef.get()) {
                            mViewRef.get().cancelLoading();
                            mViewRef.get().showData(data);
                        }
                    }
                });
            }
        }
    }

}
