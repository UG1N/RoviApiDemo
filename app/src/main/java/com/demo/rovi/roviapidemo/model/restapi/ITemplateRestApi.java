package com.demo.rovi.roviapidemo.model.restapi;

import com.demo.rovi.roviapidemo.model.templatefile.TemplateFile;

import retrofit.http.GET;
import retrofit.http.Url;
import rx.Observable;

public interface ITemplateRestApi {

    @GET
    Observable<TemplateFile> getTemplateFileRx(@Url String url);
}
