package com.qiao_techcomponent.qiaohttp;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskPoolManager {

    //阻塞队列
    private LinkedBlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<>();
    //线程池
    private ThreadPoolExecutor mExecutor;
    private static TaskPoolManager instance;

    public  static TaskPoolManager getInstance () {
        if (null == instance) {
            instance = new TaskPoolManager();
        }
        return instance;
    }


    /***
     * 添加任务到队列
     * @param r
     */
    public void execute (Runnable r) {
        if (null != r) {
            try {
                mQueue.put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    private TaskPoolManager () {
        mExecutor = new ThreadPoolExecutor(QiaoHttpConst.CORE_POOL_SIZE,QiaoHttpConst.MAXI_MUM_POOL_SIZE,QiaoHttpConst.KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(QiaoHttpConst.CAPACITY),rejectedExecutionHandler);
        mExecutor.execute(mRunnable);
    }


    /***
     * 因各种原因导致任务在一定时间内没有正常执行的任务，继续放入任务队列
     */
    private RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

            try {
                if (null != mQueue) {
                    mQueue.put(r);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /***
     * 从队列中取出任务放入线程池
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Runnable r = null;
                    r = mQueue.take();
                    if (null != r) {
                        mExecutor.execute(r);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
