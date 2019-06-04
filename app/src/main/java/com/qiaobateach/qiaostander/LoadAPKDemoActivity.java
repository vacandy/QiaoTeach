package com.qiaobateach.qiaostander;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import java.io.File;
import com.qiaobateach.R;


public class LoadAPKDemoActivity extends Activity{

    private Button mLoadBtn,mEnterBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_apk_layout);
        PluginLoadManager.getInstance().setContext(this);
        mLoadBtn = findViewById(R.id.loadBtn);
        mEnterBtn = findViewById(R.id.enterBtn);
        checkPermission();

        mLoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load ();
            }
        });
        mEnterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter();
            }
        });

        lengthOfLongestSubstring("abcabcbb");
    }

    private void load () {

        File file = new File(Environment.getExternalStorageDirectory(),"plugin.apk");
        PluginLoadManager.getInstance().loadPath(file.getAbsolutePath());
    }


    private void enter () {
        Intent intent = new Intent(this,LoadPluginActivity.class);
        intent.putExtra("className",PluginLoadManager.getInstance().getEnterActivityName());
        startActivity(intent);
    }

    public void checkPermission() {
        boolean isGranted = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //如果没有写sd卡权限
                isGranted = false;
            }
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            if (!isGranted) {
                this.requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
                                .ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        102);
            }
        }

    }


    /**
     * 查找连续字符串的最大长度
     * @param s abcabcaba
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        int []hash = new int [500];
        int max = 0;
        int i = 0, j = 0;

        while (i < s.length() && j <s.length() ) {
            Object o = s.charAt(j);
            if(hash[s.charAt(j)] == 0) {
                hash[s.charAt(j)] = 1;
                int xx = hash[s.charAt(j)];
                j++;
                max = (j - i) > max ? (j - i) : max;
            } else {
                hash[s.charAt(i)] = 0;
                i++;
            }
        }
        return max;
    }


}
