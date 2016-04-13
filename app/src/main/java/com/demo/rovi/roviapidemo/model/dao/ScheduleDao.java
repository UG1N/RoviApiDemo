package com.demo.rovi.roviapidemo.model.dao;

import com.demo.rovi.roviapidemo.model.schedule.TvSchedule;
import com.demo.rovi.roviapidemo.model.restapi.IScheduleRestApi;

import rx.Observable;

public class ScheduleDao {
    private final IScheduleRestApi mScheduleRestApi;

    public ScheduleDao(IScheduleRestApi scheduleRestApi) {
        mScheduleRestApi = scheduleRestApi;
    }

    public Observable<TvSchedule> getSchedule(String urlToLoadSchedule) {
        return mScheduleRestApi.getScheduleForChannel(urlToLoadSchedule);
    }
}
