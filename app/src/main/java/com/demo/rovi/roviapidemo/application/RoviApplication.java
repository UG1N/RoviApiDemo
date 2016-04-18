package com.demo.rovi.roviapidemo.application;

import android.app.Application;

import com.demo.rovi.roviapidemo.R;
import com.demo.rovi.roviapidemo.di.ApplicationComponent;
import com.demo.rovi.roviapidemo.model.dao.auth.RestApiClientConfiguration;
import com.demo.rovi.roviapidemo.model.templatefile.TemplateFile;

public class RoviApplication extends Application {

    private ApplicationComponent mApplicationInjector;
    private static RoviApplication instance;

    private RestApiClientConfiguration mRestApiClientConfiguration;
    private TemplateFile mTemplateFile;

    public static RoviApplication getInstance() {
        return instance;
    }

    //RoviApplication.getTemplateFile();

    public static ApplicationComponent getApplicationInjector() {
        return getInstance().mApplicationInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initRetrofit();
        initDependencyInjection();

        mApplicationInjector.inject(this);
    }

    private void initDependencyInjection() {
        mApplicationInjector = ApplicationComponent.InjectorInitializer.initialize(mRestApiClientConfiguration);
    }


    private void initRetrofit() {
        mRestApiClientConfiguration = new RestApiClientConfiguration(this);
    }

    public TemplateFile getTemplateFile() {
        if (mTemplateFile == null) {
            throw new IllegalStateException(getString(R.string.error_template_file));
        }
        return mTemplateFile;
    }

    public void setTemplateFile(TemplateFile templateFile) {
        mTemplateFile = templateFile;
    }
}
