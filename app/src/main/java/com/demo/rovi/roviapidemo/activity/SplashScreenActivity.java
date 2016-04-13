package com.demo.rovi.roviapidemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.damnhandy.uri.template.UriTemplate;
import com.demo.rovi.roviapidemo.R;
import com.demo.rovi.roviapidemo.application.RoviApplication;
import com.demo.rovi.roviapidemo.model.BackendConstants;
import com.demo.rovi.roviapidemo.model.dao.TemplateFileDao;
import com.demo.rovi.roviapidemo.model.internal.AbstractRxSubscriber;
import com.demo.rovi.roviapidemo.model.restapi.ITemplateRestApi;
import com.demo.rovi.roviapidemo.model.templatefile.TemplateFile;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SplashScreenActivity extends AppCompatActivity {

    public static final String TAG = "SplashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        String templateUrl = UriTemplate.fromTemplate(BackendConstants.URL_TO_LOAD_TEMPLATE_FILE)
                .set(BackendConstants.STRING_ID, BackendConstants.CONSUMER_KEY)
                .set(BackendConstants.STRING_CURRENT_VERSION, BackendConstants.CURRENT_TEMPLATE_VERSION)
                .expand();
        Log.e(TAG, templateUrl);

        TemplateFileDao templateFileDao = new TemplateFileDao(RoviApplication.createRestApiServiceImpl(ITemplateRestApi.class));
        templateFileDao.getTemplateFileRx(templateUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbstractRxSubscriber<TemplateFile>() {
                    @Override
                    public void onNext(TemplateFile templateFile) {
                        RoviApplication.getInstance().setTemplateFile(templateFile);

                        Intent mainActivity = new Intent(SplashScreenActivity.this, ChannelListActivity.class);
                        SplashScreenActivity.this.startActivity(mainActivity);
                        SplashScreenActivity.this.finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SplashScreenActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        SplashScreenActivity.this.finish();
                    }
                });
    }
}
