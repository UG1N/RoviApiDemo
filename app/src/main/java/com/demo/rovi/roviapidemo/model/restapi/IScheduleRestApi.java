package com.demo.rovi.roviapidemo.model.restapi;

import com.demo.rovi.roviapidemo.model.TvSchedule.TvSchedule;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Url;

public interface IScheduleRestApi {

    @GET
    Call<TvSchedule> getScheduleForChannel(@Url String url);
}
