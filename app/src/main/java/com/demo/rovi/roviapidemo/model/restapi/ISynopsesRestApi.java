package com.demo.rovi.roviapidemo.model.restapi;

import com.demo.rovi.roviapidemo.model.synopses.AirSynopsis;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Url;

public interface ISynopsesRestApi {

    @GET
    Call<AirSynopsis> getDescription(@Url String url);
}
