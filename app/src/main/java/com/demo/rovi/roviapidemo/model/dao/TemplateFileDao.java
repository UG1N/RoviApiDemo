package com.demo.rovi.roviapidemo.model.dao;

import com.demo.rovi.roviapidemo.model.restapi.ITemplateRestApi;
import com.demo.rovi.roviapidemo.model.templatefile.TemplateFile;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class TemplateFileDao {

    private final ITemplateRestApi mITemplateRestApi;

    @Inject
    public TemplateFileDao(ITemplateRestApi ITemplateRestApi) {
        this.mITemplateRestApi = ITemplateRestApi;
    }

    public Observable<TemplateFile> getTemplateFileRx(String urlToLoadTemplateFile) {
        return mITemplateRestApi.getTemplateFileRx(urlToLoadTemplateFile);
    }
}
