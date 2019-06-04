package com.qiao_basemodule.mvp;

import java.util.List;

public interface IBaseModel {

    void loadData (DataLoadListener l);
    interface DataLoadListener {
        void onComplete (List<Data> data);
    }
}
