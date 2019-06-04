package com.qiaobateach.mvp;

import com.qiao_basemodule.mvp.Data;
import com.qiao_basemodule.mvp.IBaseModel;

import java.util.ArrayList;
import java.util.List;

public class DemoModelImpl implements IBaseModel{
    @Override
    public void loadData(DataLoadListener l) {
        List<Data> data = new ArrayList<>();
        data.add(new Data("StringOne"));
        data.add(new Data("StringTwo"));
        data.add(new Data("StringThree"));
        l.onComplete(data);
    }
}
