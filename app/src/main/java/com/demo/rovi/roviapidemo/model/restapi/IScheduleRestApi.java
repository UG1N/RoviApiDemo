package com.demo.rovi.roviapidemo.model.restapi;

import com.demo.rovi.roviapidemo.model.schedule.TvSchedule;

import retrofit.http.GET;
import retrofit.http.Url;
import rx.Observable;

public interface IScheduleRestApi {

    @GET
    Observable<TvSchedule> getScheduleForChannel(@Url String url);
}
