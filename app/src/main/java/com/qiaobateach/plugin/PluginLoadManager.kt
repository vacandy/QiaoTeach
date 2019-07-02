package com.qiaobateach.plugin

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources

import java.io.File
import java.lang.reflect.Method

import dalvik.system.DexClassLoader

/****
 * 获取插件的加载类等信息
 */
class PluginLoadManager private constructor() {


    private var mDexClassLoader: DexClassLoader? = null
    private var mResources: Resources? = null
    private var mContext: Context? = null
    private var enterActivityName: String? = null


    fun getmResources(): Resources? {
        return mResources
    }

    fun setmResources(mResources: Resources) {
        this.mResources = mResources
    }


    fun getmDexClassLoader(): DexClassLoader? {
        return mDexClassLoader
    }

    fun setmDexClassLoader(mDexClassLoader: DexClassLoader) {
        this.mDexClassLoader = mDexClassLoader
    }


    /***
     * @Deprecated
     * public Resources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
     * this(null);
     * mResourcesImpl = new ResourcesImpl(assets, metrics, config, new DisplayAdjustments());
     * }
     * @param apkPath
     */
    fun loadPath(apkPath: String) {

        val packageManager = mContext!!.packageManager
        val packageInfo = packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES)
        enterActivityName = packageInfo.activities[0].name

        val outCachePath = mContext!!.getDir("dex", Context.MODE_PRIVATE)
        mDexClassLoader = DexClassLoader(apkPath, outCachePath.absolutePath, null, mContext!!.classLoader)
        var assetManager: AssetManager? = null
        try {
            assetManager = AssetManager::class.java.newInstance()
            val addAssetPath = AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
            addAssetPath.invoke(assetManager, apkPath)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mResources = Resources(assetManager, mContext!!.resources.displayMetrics, mContext!!.resources.configuration)
    }


    fun setContext(context: Context) {
        mContext = context.applicationContext
    }

    companion object {

        val instance = PluginLoadManager()
    }
}
