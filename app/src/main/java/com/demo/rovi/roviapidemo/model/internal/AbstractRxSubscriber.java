package com.demo.rovi.roviapidemo.model.internal;

import rx.Subscriber;

public abstract class AbstractRxSubscriber<T> extends Subscriber<T> {

    @Override
    public void onNext(T t) {
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        // TODO: 08.04.2016 Use Timber logging framework
    }
}
