package com.demo.rovi.roviapidemo.di;

import com.demo.rovi.roviapidemo.activity.ChannelListActivity;
import com.demo.rovi.roviapidemo.activity.SplashScreenActivity;
import com.demo.rovi.roviapidemo.application.RoviApplication;
import com.demo.rovi.roviapidemo.model.dao.auth.RestApiClientConfiguration;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RestApiModule.class})
public interface ApplicationComponent {

    void inject(RoviApplication application);

    void inject(ChannelListActivity activity);

    void inject(SplashScreenActivity activity);

    final class InjectorInitializer {
        public static ApplicationComponent initialize(RestApiClientConfiguration restApiClientConfiguration) {
            return DaggerApplicationComponent
                    .builder()
                    .restApiModule(new RestApiModule(restApiClientConfiguration))
                    .build();
        }
    }
}
