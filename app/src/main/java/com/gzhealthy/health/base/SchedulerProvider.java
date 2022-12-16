package com.gzhealthy.health.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gzhealthy.health.protocol.BaseSchedulerProvider;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SchedulerProvider implements BaseSchedulerProvider {

    @Nullable
    private static SchedulerProvider _instance;

    private SchedulerProvider() {
    }

    public static synchronized SchedulerProvider getInstance() {
        if (_instance == null) {
            _instance = new SchedulerProvider();
        }
        return _instance;
    }


    @NonNull
    @Override
    public Scheduler immediate() {
        return Schedulers.immediate();
    }

    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

}
