package com.demo.rovi.roviapidemo.model.dao;

import android.widget.Toast;

import com.demo.rovi.roviapidemo.model.Template.TemplateFile;
import com.demo.rovi.roviapidemo.model.restapi.IDataLoadingCallback;
import com.demo.rovi.roviapidemo.model.restapi.ITemplateRestApi;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TemplateFileDao {
    private final ITemplateRestApi mITemplateRestApi;

    public TemplateFileDao(ITemplateRestApi ITemplateRestApi) {
        this.mITemplateRestApi = ITemplateRestApi;
    }

    public void getTemplateFile(String urlToLoadTemplateFile, final IDataLoadingCallback<TemplateFile> loadingCallback) {
        mITemplateRestApi.getTemplateFile(urlToLoadTemplateFile).enqueue(
                new Callback<TemplateFile>() {
                    @Override
                    public void onResponse(Response<TemplateFile> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            loadingCallback.onResult(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // TODO: 28.03.2016
                        loadingCallback.onFailure(t);
                    }
                }
        );
    }

    public void getTemplateFileRx(String urlToLoadTemplateFile,
                                  final IDataLoadingCallback<TemplateFile> loadingCallback) {
        mITemplateRestApi.getTemplateFileRx(urlToLoadTemplateFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io()).subscribe(new Subscriber<TemplateFile>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TemplateFile templateFile) {
                loadingCallback.onResult(templateFile);
            }
        });
    }
}
