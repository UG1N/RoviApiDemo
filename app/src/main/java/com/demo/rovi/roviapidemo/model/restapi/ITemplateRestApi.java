package com.demo.rovi.roviapidemo.model.restapi;

import com.demo.rovi.roviapidemo.model.Template.TemplateFile;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Url;

public interface ITemplateRestApi {

    @GET
    Call<TemplateFile> getTemplateFile(@Url String url);
}
