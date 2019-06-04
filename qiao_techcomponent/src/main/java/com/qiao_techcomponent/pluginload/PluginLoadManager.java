package com.qiao_techcomponent.pluginload;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/****
 * 获取插件的加载类等信息
 */
public class PluginLoadManager {

    private static final PluginLoadManager ourInstance = new PluginLoadManager();


    //Dex包加载
    private DexClassLoader mDexClassLoader;
    //资源加载
    private Resources mResources;
    private Context mContext;
    private String enterActivityName;


    public String getEnterActivityName() {
        return enterActivityName;
    }

    public void setEnterActivityName(String enterActivityName) {
        this.enterActivityName = enterActivityName;
    }



    public Resources getmResources() {
        return mResources;
    }

    public void setmResources(Resources mResources) {
        this.mResources = mResources;
    }


    public DexClassLoader getmDexClassLoader() {
        return mDexClassLoader;
    }

    public void setmDexClassLoader(DexClassLoader mDexClassLoader) {
        this.mDexClassLoader = mDexClassLoader;
    }


    public static PluginLoadManager getInstance() {
        return ourInstance;
    }

    private PluginLoadManager() {

    }


    /***
     * @Deprecated
      public Resources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
     this(null);
     mResourcesImpl = new ResourcesImpl(assets, metrics, config, new DisplayAdjustments());
    }
     * @param apkPath
     */
    /***
     * 根据apk的路径获取 mDexClassLoader assetManager Resources
     * @param apkPath
     */
    public void loadPath (String apkPath) {

        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkPath,PackageManager.GET_ACTIVITIES);
        enterActivityName = packageInfo.activities[0].name;

        File outCachePath = mContext.getDir("dex",Context.MODE_PRIVATE);
        mDexClassLoader = new DexClassLoader(apkPath,outCachePath.getAbsolutePath(),null,mContext.getClassLoader());
        AssetManager assetManager = null;
        try {
            assetManager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
            addAssetPath.invoke(assetManager,apkPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mResources = new Resources(assetManager,mContext.getResources().getDisplayMetrics()
                ,mContext.getResources().getConfiguration());
    }


    public void setContext (Context context) {
        mContext = context.getApplicationContext();
    }
}
