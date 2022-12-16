package com.gzhealthy.health.protocol;

import androidx.annotation.NonNull;

import rx.Scheduler;

public interface BaseSchedulerProvider {

    @NonNull
    Scheduler immediate();

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();

}
