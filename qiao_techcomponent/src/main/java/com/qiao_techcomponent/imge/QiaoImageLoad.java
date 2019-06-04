package com.qiao_techcomponent.imge;

import android.content.Context;
import android.graphics.Bitmap;

public class QiaoImageLoad {


    public static Bitmap ImageLoad (Context context,String imageUrl,int defaultBitmap) {

        Bitmap bitmap = QiaoImgCache.getInstance().getBitmapFromMemory(imageUrl);
        if(null == bitmap){
            //如果内存没数据，就去复用池找
            Bitmap reuseable = QiaoImgCache.getInstance().getReuseable(60,60,1);
            //reuseable能复用的内存
            //从磁盘找
            bitmap = QiaoImgCache.getInstance().getBitmapFromDisk(imageUrl,reuseable);
            //如果磁盘中也没缓存,就从网络下载
            if(null==bitmap) {
                bitmap = QiaoImgResize.resizeBitmap(context,defaultBitmap,80,80,false,reuseable);
                QiaoImgCache.getInstance().putBitmapToMemeory(imageUrl,bitmap);
                QiaoImgCache.getInstance().putBitmapToDisk(imageUrl,bitmap,50);
            }
        }
        return bitmap;
    }
}
