package com.gzhealthy.health.base;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Justin_Liu
 * on 2021/4/25
 */
public class RxManager {
    private RxBus mRxBus = RxBus.getInstance();
    private List<RxEntity> mObservables = new LinkedList();
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public RxManager() {
    }

    public Subscription onRxEvent(@NonNull String s1, @NonNull Action1<Object> action1) {
        return this.onRxEvent(new String[]{s1}, action1);
    }

    public Subscription onRxEvent(@NonNull String s1, @NonNull String s2, @NonNull Action1<Object> action1) {
        return this.onRxEvent(new String[]{s1, s2}, action1);
    }

    public Subscription onRxEvent(@NonNull String s1, @NonNull String s2, @NonNull String s3, @NonNull Action1<Object> action1) {
        return this.onRxEvent(new String[]{s1, s2, s3}, action1);
    }

    public Subscription onRxEvent(@NonNull String[] eventNameList, @NonNull Action1<Object> action1) {
        Observable<?> mObservable = this.mRxBus.register(eventNameList);
        String[] var4 = eventNameList;
        int var5 = eventNameList.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String eventName = var4[var6];
            RxManager.RxEntity entity = new RxManager.RxEntity(eventName, mObservable);
            this.mObservables.add(entity);
        }

        Subscription subscription = mObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new Action1<Throwable>() {
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        this.mCompositeSubscription.add(subscription);
        return subscription;
    }

    public Observable<?> onRxEvent(@NonNull String eventName) {
        Observable<?> mObservable = this.mRxBus.register(new String[]{eventName});
        RxManager.RxEntity entity = new RxManager.RxEntity(eventName, mObservable);
        this.mObservables.add(entity);
        return mObservable.observeOn(AndroidSchedulers.mainThread());
    }

    public void add(@NonNull Subscription m) {
        this.mCompositeSubscription.add(m);
    }

    public void clear() {
        this.mCompositeSubscription.unsubscribe();
        Iterator var1 = this.mObservables.iterator();

        while(var1.hasNext()) {
            RxManager.RxEntity entity = (RxManager.RxEntity)var1.next();
            this.mRxBus.unregister(entity.name, entity.observable);
        }

    }

    private class RxEntity {
        String name;
        Observable<?> observable;

        RxEntity(String name, Observable<?> observable) {
            this.name = name;
            this.observable = observable;
        }
    }
}
