package com.gzhealthy.health.protocol;

import rx.Subscription;

public interface LifeSubscription {
    void bindSubscription(Subscription subscription);
}
