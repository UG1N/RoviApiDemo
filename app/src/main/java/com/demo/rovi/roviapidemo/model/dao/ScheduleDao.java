package com.demo.rovi.roviapidemo.model.dao;

import com.demo.rovi.roviapidemo.model.TvSchedule.TvSchedule;
import com.demo.rovi.roviapidemo.model.restapi.IDataLoadingCallback;
import com.demo.rovi.roviapidemo.model.restapi.IScheduleRestApi;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ScheduleDao {
    private final IScheduleRestApi mScheduleRestApi;

    public ScheduleDao(IScheduleRestApi scheduleRestApi) {
        mScheduleRestApi = scheduleRestApi;
    }

    public void getSchedule(String urlToLoadSchedule, final IDataLoadingCallback<TvSchedule> scheduleIDataLoadingCallback) {
        mScheduleRestApi.getScheduleForChannel(urlToLoadSchedule).enqueue(
                new Callback<TvSchedule>() {
                    @Override
                    public void onResponse(Response<TvSchedule> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            scheduleIDataLoadingCallback.onResult(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        scheduleIDataLoadingCallback.onFailure(t);
                    }
                }
        );
    }
}
