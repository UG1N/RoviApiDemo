package com.demo.rovi.roviapidemo.model.dao;

import com.demo.rovi.roviapidemo.model.restapi.IScheduleRestApi;
import com.demo.rovi.roviapidemo.model.schedule.TvSchedule;

import javax.inject.Inject;

import rx.Observable;

public class ScheduleDao {
    private final IScheduleRestApi mScheduleRestApi;

    @Inject
    public ScheduleDao(IScheduleRestApi scheduleRestApi) {
        mScheduleRestApi = scheduleRestApi;
    }

    public Observable<TvSchedule> getSchedule(String urlToLoadSchedule) {
        return mScheduleRestApi.getScheduleForChannel(urlToLoadSchedule);
    }
}
