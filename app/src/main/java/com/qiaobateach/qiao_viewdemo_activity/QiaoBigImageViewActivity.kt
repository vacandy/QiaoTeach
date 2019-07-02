package com.qiaobateach.qiao_viewdemo_activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.Toast

import com.qiao_techcomponent.imge.view.QiaoBigImageView
import com.qiaobateach.MainActivity
import com.qiaobateach.R

import java.io.IOException
import java.io.InputStream

class QiaoBigImageViewActivity : Activity() {

    private var qiaoBigImageView: QiaoBigImageView? = null
    private var btn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.big_imageview)
        qiaoBigImageView = findViewById(R.id.qiaoBigImg)
        btn = findViewById<View>(R.id.btn) as Button
        loadBigImg(qiaoBigImageView)

        var intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()
    }

    private fun loadBigImg(qiaoBigImageView: QiaoBigImageView?) {
        var `is`: InputStream? = null
        try {
            //加载图片
            `is` = assets.open("big.png")
            qiaoBigImageView!!.setBitmap(`is`)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
