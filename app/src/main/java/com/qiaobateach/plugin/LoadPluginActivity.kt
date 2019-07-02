package com.qiaobateach.plugin

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle

import com.qiao_stander.QiaoInterface
import dalvik.system.DexClassLoader

import java.lang.reflect.Constructor

class LoadPluginActivity : Activity() {


    private var className: String? = null
    private var mQiaoInterface: QiaoInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        className = intent.getStringExtra("className")

        try {
            val activity = classLoader!!.loadClass(className)
            val constructor = activity.getConstructor(*arrayOf())
            val instance = constructor.newInstance(*arrayOf())
            mQiaoInterface = instance as QiaoInterface
            mQiaoInterface!!.attach(this)
            val bundle = Bundle()
            mQiaoInterface!!.onCreate(bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onStart() {
        super.onStart()
        mQiaoInterface!!.onStart()
    }


    override fun onResume() {
        super.onResume()
        mQiaoInterface!!.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mQiaoInterface!!.onDestroy()
    }


    override fun onRestart() {
        super.onRestart()

    }


    override fun onPause() {
        super.onPause()
        mQiaoInterface!!.onPause()
    }

    override fun onStop() {
        super.onStop()
        mQiaoInterface!!.onStop()
    }

    override fun getClassLoader(): DexClassLoader? {
        return PluginLoadManager.instance.getmDexClassLoader();
//        return PluginLoadManager.getInstance().getmDexClassLoader()
    }

    override fun getResources(): Resources? {
        return PluginLoadManager.instance.getmResources();
    }


    override fun startActivity(intent: Intent) {
        val className = intent.getStringExtra("className")
        /***
         * 跳转自己，由代理Activity跳转到目标页面，为了重新赋予插件要跳转的activity的生命周期
         */
        val i = Intent(this, LoadPluginActivity::class.java)
        i.putExtra("className", className)
        super.startActivity(i)
    }
}
