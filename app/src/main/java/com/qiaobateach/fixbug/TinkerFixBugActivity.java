package com.qiaobateach.fixbug;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.qiao_techcomponent.qiao_tinkerfix.FileUtils;
import com.qiao_techcomponent.qiao_tinkerfix.QiaoFixManager;

import java.io.File;
import java.io.IOException;

public class TinkerFixBugActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fixBug();
    }


    private void fixBug() {
        //1 从服务器下载dex文件 比如v1.1修复包文件（classes2.dex）
        File sourceFile = new File(Environment.getExternalStorageDirectory(), "classes.dex");
        // 目标路径：私有目录
        //getDir("odex", Context.MODE_PRIVATE) data/user/0/包名/app_odex
        File targetFile = new File(getDir("odex",
                Context.MODE_PRIVATE).getAbsolutePath() + File.separator + "classes.dex");
        if (targetFile.exists()) {
            targetFile.delete();
        }
        try {
            // 复制dex到私有目录
            FileUtils.copyFile(sourceFile, targetFile);
            QiaoFixManager.loadFixedDex(this);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
