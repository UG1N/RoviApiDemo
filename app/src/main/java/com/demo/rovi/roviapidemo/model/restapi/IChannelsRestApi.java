package com.demo.rovi.roviapidemo.model.restapi;

import com.demo.rovi.roviapidemo.model.TvChannels.TvChannels;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Url;

/**
 * @author Alexey Kovalev
 * @since 28.03.2016.
 */
public interface IChannelsRestApi {
    @GET
    Call<TvChannels> getChannelsDataFromUrl(@Url String url);
}
