package com.qiaobachild;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.qiaobachild.base.ChildBaseActivity;

public class MainActivity extends ChildBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.enterBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(that,SecondActivity.class);
                startActivity(intent);
            }
        });
        Button startServiceBtn = findViewById(R.id.startService);
        startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(that,ChildService.class));
            }
        });
        Button broadCastBtn = findViewById(R.id.broadCastBtn);
        broadCastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction("com.qiaobachild.MainActivity");
                sendBroadcast(intent);
            }
        });
        registerBroadCast();
    }

    private void registerBroadCast () {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.qiaobachild.MainActivity");
        registerReceiver(new ChildBroadCast(),intentFilter);
    }
}
