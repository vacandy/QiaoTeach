package com.qiaobachild;

import android.os.Bundle;

import com.qiaobachild.base.ChildBaseActivity;

public class SecondActivity extends ChildBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);
    }

}
