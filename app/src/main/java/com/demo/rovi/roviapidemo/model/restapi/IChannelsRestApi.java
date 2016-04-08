package com.demo.rovi.roviapidemo.model.restapi;

import com.demo.rovi.roviapidemo.model.channels.TvChannels;

import retrofit.http.GET;
import retrofit.http.Url;
import rx.Observable;

/**
 * @author Alexey Kovalev
 * @since 28.03.2016.
 */
public interface IChannelsRestApi {
    @GET
    Observable<TvChannels> getChannelsDataFromUrl(@Url String url);
}
