package com.demo.rovi.roviapidemo.application;

import android.app.Application;

import com.demo.rovi.roviapidemo.model.dao.auth.RestApiClientConfiguration;

public class RoviApplication extends Application {

    private static RoviApplication instance;
    private RestApiClientConfiguration mRestApiClientConfiguration;

    public static RoviApplication getInstance() {
        return instance;
    }

    public static <T> T createRestApiServiceImpl(Class<T> serviceApiInterface) {
        return instance.mRestApiClientConfiguration.createRestApiServiceImpl(serviceApiInterface);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initRetrofit();
    }

    private void initRetrofit() {
        mRestApiClientConfiguration = new RestApiClientConfiguration(this);
    }
}
