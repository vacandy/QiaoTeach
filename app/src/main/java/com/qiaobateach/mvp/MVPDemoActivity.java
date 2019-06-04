package com.qiaobateach.mvp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.qiao_basemodule.mvp.BaseActivity;
import com.qiao_basemodule.mvp.Data;

import java.util.List;

public class MVPDemoActivity extends BaseActivity<IDemoBaseView,DemoPresent<IDemoBaseView>> implements IDemoBaseView{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        present.fetchData();
    }

    @Override
    public void showLoading() {
        Toast.makeText(this,"loading...",Toast.LENGTH_LONG).show();
    }

    @Override
    public void cancelLoading() {
        Toast.makeText(this,"success",Toast.LENGTH_LONG).show();
    }

    @Override
    public void showData(List<Data> data) {
        Toast.makeText(this,"show UI (bind data)",Toast.LENGTH_LONG).show();
    }

    @Override
    protected DemoPresent<IDemoBaseView> createPresent() {
        return new DemoPresent<>();
    }
}
