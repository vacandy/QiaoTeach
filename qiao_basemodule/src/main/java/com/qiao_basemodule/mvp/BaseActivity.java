package com.qiao_basemodule.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/***
 * MVP架构设计-Activity基类
 */
public abstract class BaseActivity<V,T extends BasePresent<V>> extends AppCompatActivity{


    public T present;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        present = createPresent();
        present.attachView((V)this);
    }

    protected abstract T createPresent () ;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        present.detachView();
    }
}
