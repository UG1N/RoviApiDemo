package com.demo.rovi.roviapidemo.model.dao;

import com.demo.rovi.roviapidemo.model.Template.TemplateFile;
import com.demo.rovi.roviapidemo.model.restapi.IDataLoadingCallback;
import com.demo.rovi.roviapidemo.model.restapi.ITemplateRestApi;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TemplateFileDao {
    private final ITemplateRestApi mITemplateRestApi;

    public TemplateFileDao(ITemplateRestApi ITemplateRestApi) {
        this.mITemplateRestApi = ITemplateRestApi;
    }

    public void getTemplateFileRx(String urlToLoadTemplateFile,
                                  final IDataLoadingCallback<TemplateFile> loadingCallback) {
        mITemplateRestApi.getTemplateFileRx(urlToLoadTemplateFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<TemplateFile>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingCallback.onFailure(e);
                    }

                    @Override
                    public void onNext(TemplateFile templateFile) {
                        loadingCallback.onResult(templateFile);
                    }
                });
    }
}
