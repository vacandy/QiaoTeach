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

public class QiaoBigImageViewActivity extends Activity{

    private QiaoBigImageView qiaoBigImageView;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_imageview);
        qiaoBigImageView = findViewById(R.id.qiaoBigImg);
        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QiaoBigImageViewActivity.this,TestActivity.class);
                startActivity(i);
            }
        });
        loadBigImg(qiaoBigImageView);
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
