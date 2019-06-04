package com.qiao_basemodule.mvp;

import java.util.List;

public interface IBaseView {

    void showLoading ();

    void cancelLoading ();

    void showData (List<Data> data);

}
