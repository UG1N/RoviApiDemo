package com.demo.rovi.roviapidemo.model.dao;

import com.demo.rovi.roviapidemo.model.restapi.ITemplateRestApi;
import com.demo.rovi.roviapidemo.model.templatefile.TemplateFile;

import rx.Observable;

public class TemplateFileDao {
    private final ITemplateRestApi mITemplateRestApi;

    public TemplateFileDao(ITemplateRestApi ITemplateRestApi) {
        this.mITemplateRestApi = ITemplateRestApi;
    }

    public Observable<TemplateFile> getTemplateFileRx(String urlToLoadTemplateFile) {
        return mITemplateRestApi.getTemplateFileRx(urlToLoadTemplateFile);
    }
}
