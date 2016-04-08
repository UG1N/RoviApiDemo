package com.demo.rovi.roviapidemo.model.dao;

import com.demo.rovi.roviapidemo.model.schedule.TvSchedule;
import com.demo.rovi.roviapidemo.model.restapi.IDataLoadingCallback;
import com.demo.rovi.roviapidemo.model.restapi.IScheduleRestApi;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ScheduleDao {
    private final IScheduleRestApi mScheduleRestApi;

    public ScheduleDao(IScheduleRestApi scheduleRestApi) {
        mScheduleRestApi = scheduleRestApi;
    }

    public void getSchedule(String urlToLoadSchedule, final IDataLoadingCallback<TvSchedule> scheduleIDataLoadingCallback) {
        mScheduleRestApi.getScheduleForChannel(urlToLoadSchedule).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<TvSchedule>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   scheduleIDataLoadingCallback.onFailure(e);
                               }

                               @Override
                               public void onNext(TvSchedule tvSchedule) {
                                   scheduleIDataLoadingCallback.onResult(tvSchedule);
                               }
                           }

                );
    }
}
