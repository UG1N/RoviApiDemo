package com.demo.rovi.roviapidemo.di;

import com.demo.rovi.roviapidemo.model.dao.auth.RestApiClientConfiguration;
import com.demo.rovi.roviapidemo.model.restapi.IChannelsRestApi;
import com.demo.rovi.roviapidemo.model.restapi.IScheduleRestApi;
import com.demo.rovi.roviapidemo.model.restapi.ISynopsesRestApi;
import com.demo.rovi.roviapidemo.model.restapi.ITemplateRestApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RestApiModule {
    private final RestApiClientConfiguration mRestApiClientConfiguration;

    public RestApiModule(RestApiClientConfiguration restApiClientConfiguration) {
        mRestApiClientConfiguration = restApiClientConfiguration;
    }

    @Singleton
    @Provides
    ITemplateRestApi provideTemplateRestApi() {
        return mRestApiClientConfiguration.createRestApiServiceImpl(ITemplateRestApi.class);
    }

    @Singleton
    @Provides
    IChannelsRestApi provideChannelsRestApi() {
        return mRestApiClientConfiguration.createRestApiServiceImpl(IChannelsRestApi.class);
    }

    @Singleton
    @Provides
    IScheduleRestApi provideScheduleRestApi() {
        return mRestApiClientConfiguration.createRestApiServiceImpl(IScheduleRestApi.class);
    }

    @Singleton
    @Provides
    ISynopsesRestApi provideSynopsisRestApi() {
        return mRestApiClientConfiguration.createRestApiServiceImpl(ISynopsesRestApi.class);
    }
}
