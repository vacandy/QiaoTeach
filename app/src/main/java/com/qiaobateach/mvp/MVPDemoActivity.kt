package com.qiaobateach.mvp


import android.os.Bundle
import android.widget.Toast

import com.qiao_basemodule.mvp.BaseActivity
import com.qiao_basemodule.mvp.Data

class MVPDemoActivity : BaseActivity<IDemoBaseView, DemoPresent<IDemoBaseView>>(), IDemoBaseView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        present.fetchData()
    }

    override fun showLoading() {
        Toast.makeText(this, "loading...", Toast.LENGTH_LONG).show()
    }

    override fun cancelLoading() {
        Toast.makeText(this, "success", Toast.LENGTH_LONG).show()
    }

    override fun showData(data: List<Data>) {
        Toast.makeText(this, "show UI (bind data)", Toast.LENGTH_LONG).show()
    }

    override fun createPresent(): DemoPresent<IDemoBaseView> {
        return DemoPresent()
    }


}
