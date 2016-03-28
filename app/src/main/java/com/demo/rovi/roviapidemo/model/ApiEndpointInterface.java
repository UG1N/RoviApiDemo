package com.demo.rovi.roviapidemo.model;

import com.demo.rovi.roviapidemo.model.TvChannels.TvChannels;
import com.demo.rovi.roviapidemo.model.TvSchedule.TvSchedule;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiEndpointInterface {
    @GET
    Call<TvChannels> getChannelsDataFromUrl(@Url String url);

    @GET
    Call<TvSchedule> getScheduleDataFromUrl(@Url String url);
}
