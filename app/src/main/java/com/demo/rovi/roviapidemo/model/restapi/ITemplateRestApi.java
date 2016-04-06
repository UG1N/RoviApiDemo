package com.demo.rovi.roviapidemo.model.restapi;

import com.demo.rovi.roviapidemo.model.Template.TemplateFile;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Url;
import rx.Observable;

public interface ITemplateRestApi {

    @GET
    Call<TemplateFile> getTemplateFile(@Url String url);

    @GET
    Observable<TemplateFile> getTemplateFileRx(@Url String url);
}
