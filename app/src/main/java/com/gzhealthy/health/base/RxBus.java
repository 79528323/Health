package com.gzhealthy.health.base;

import com.gzhealthy.health.logger.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.NonNull;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by Justin_Liu
 * on 2021/4/25
 */
public class RxBus {
    private final String TAG = this.getClass().getSimpleName();
    public static final int EMPTY_EVENT = -1;
    private static volatile RxBus defaultInstance;
    private ConcurrentHashMap<String, List<Subject>> subjectMapper = new ConcurrentHashMap();

    public RxBus() {
    }

    public static RxBus getInstance() {
        RxBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            Class var1 = RxBus.class;
            synchronized(RxBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxBus();
                    defaultInstance = rxBus;
                }
            }
        }

        return rxBus;
    }

    public <T> Observable<T> register(@NonNull String... eventNameList) {
        Subject<T, T> subject = PublishSubject.create();
        String[] var3 = eventNameList;
        int var4 = eventNameList.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String eventName = var3[var5];
            List<Subject> subjectList = (List)this.subjectMapper.get(eventName);
            if (null == subjectList) {
                subjectList = new ArrayList();
                this.subjectMapper.put(eventName, subjectList);
            }

            ((List)subjectList).add(subject);
            Logger.d(this.TAG, "register eventName : " + eventName + "  size:" + ((List)subjectList).size());
        }

        return subject;
    }

    public void unregister(@NonNull String eventName) {
        List<Subject> subjects = (List)this.subjectMapper.get(eventName);
        if (null != subjects) {
            this.subjectMapper.remove(eventName);
        }

    }

    public RxBus unregister(@NonNull String eventName, @NonNull Observable<?> observable) {
        if (null == observable) {
            return getInstance();
        } else {
            List<Subject> subjects = (List)this.subjectMapper.get(eventName);
            if (null != subjects) {
                subjects.remove((Subject)observable);
                if (this.isEmpty(subjects)) {
                    this.subjectMapper.remove(eventName);
                    Logger.d(this.TAG, "unregister eventName : " + eventName + "  size:" + subjects.size());
                }
            }

            return getInstance();
        }
    }

    public void postEmpty(@NonNull String eventName) {
        this.post(eventName, -1);
    }

    public void post(@NonNull String eventName, @NonNull Object content) {
        Logger.d(this.TAG, "post eventName : " + eventName);
        List<Subject> subjectList = (List)this.subjectMapper.get(eventName);
        if (!this.isEmpty(subjectList)) {
            Iterator var4 = subjectList.iterator();

            while(var4.hasNext()) {
                Subject subject = (Subject)var4.next();
                Logger.d(this.TAG, "onEvent eventName : " + eventName);
                subject.onNext(content);
            }
        }

    }

    private boolean isEmpty(Collection<Subject> collection) {
        return null == collection || collection.isEmpty();
    }
}
