package com.qiaobateach.qiao_viewdemo_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qiao_techcomponent.imge.view.QiaoBigImageView;
import com.qiaobateach.R;

import java.io.IOException;
import java.io.InputStream;

public class TestActivity extends Activity{

    private QiaoBigImageView qiaoBigImageView;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_imageview);
        qiaoBigImageView = findViewById(R.id.qiaoBigImg);
        loadBigImg(qiaoBigImageView);
        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        mHandler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }





    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Toast.makeText(TestActivity.this,"show",5000).show();
            }
        }
    };


    private void loadBigImg (QiaoBigImageView qiaoBigImageView) {
        InputStream is = null;
        try{
            //加载图片
            is = getAssets().open("big.png");
            qiaoBigImageView.setBitmap(is);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
