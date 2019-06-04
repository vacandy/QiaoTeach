package com.qiao_basemodule.mvp;

import java.lang.ref.WeakReference;

/***
 * 通过弱引用 解决mvp架构模式中可能会造成的Activity内存泄漏问题
 * @param <T>
 */
public class BasePresent<T> {

    protected WeakReference<T> mViewRef;
    /**
     * 进行绑定
     */

    public void attachView (T view) {
        mViewRef = new WeakReference<T>(view);
    }


    /**
     * 进行解绑
     */

    public void detachView () {
        mViewRef.clear();
    }
}
