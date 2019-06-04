package com.qiao_struct.fragmentmsg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class DemoActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void setFunctionForFm (String tag) {

        FragmentManager fm = getSupportFragmentManager();
        BaseFragment baseFragment = (BaseFragment) fm.findFragmentByTag(tag);
        FrmFunctionManager frmFunctionManager = FrmFunctionManager.getInstance();
        baseFragment.setFrmFunctionManager(frmFunctionManager);
        baseFragment.setFrmFunctionManager(frmFunctionManager.addFunction(new FrmFunctonResult(FragemenOne.INSTANCE) {
            @Override
            public void function() {


            }
        }));
    }


}
