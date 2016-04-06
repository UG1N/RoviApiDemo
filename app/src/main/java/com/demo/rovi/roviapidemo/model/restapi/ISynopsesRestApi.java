package com.demo.rovi.roviapidemo.model.restapi;

import com.demo.rovi.roviapidemo.model.synopses.AirSynopsis;

import retrofit.http.GET;
import retrofit.http.Url;
import rx.Observable;

public interface ISynopsesRestApi {

    @GET
    Observable<AirSynopsis> getDescription(@Url String url);
}
