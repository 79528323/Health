package com.gzhealthy.health.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {

    private static ThreadPool mInstance;

    //单例模式，获取线程池的实例
    public static ThreadPool getInstance() {
        if (mInstance == null) {
            synchronized (ThreadPool.class) {
                if (mInstance == null) {
                    int threadCount = Runtime.getRuntime().availableProcessors();
                    mInstance = new ThreadPool(threadCount, threadCount, 1L);
                }
            }
        }
        return mInstance;
    }


    public static class ThreadPool {

        private ThreadPoolExecutor mExecutor;
        private int mCorePoolSize;
        private int mMaximumPoolSize;
        private long mKeepAliveTime;

        public ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.mCorePoolSize = corePoolSize;
            this.mMaximumPoolSize = maximumPoolSize;
            this.mKeepAliveTime = keepAliveTime;
        }

        public void execute(Runnable r) {
            if (mExecutor == null) {
                //参1:核心线程数;参2:最大线程数;参3:线程休眠时间;参4:时间单位;参5:线程队列;参6:生产线程的工厂;参7:线程异常处理策略
                mExecutor = new ThreadPoolExecutor(
                        mCorePoolSize, mMaximumPoolSize, mKeepAliveTime, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.AbortPolicy());
            }
            if (mExecutor != null) {
                mExecutor.execute(r);
            }
        }

        //取消线程
        public void cancel(Runnable r) {
            if (mExecutor != null) {
                mExecutor.getQueue().remove(r);
            }
        }
    }
}