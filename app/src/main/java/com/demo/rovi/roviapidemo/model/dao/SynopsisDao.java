package com.demo.rovi.roviapidemo.model.dao;

import com.demo.rovi.roviapidemo.model.restapi.IDataLoadingCallback;
import com.demo.rovi.roviapidemo.model.restapi.ISynopsesRestApi;
import com.demo.rovi.roviapidemo.model.synopses.AirSynopsis;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SynopsisDao {

    private final ISynopsesRestApi mISynopsesRestApi;

    public SynopsisDao(ISynopsesRestApi iSynopsesRestApi) {
        mISynopsesRestApi = iSynopsesRestApi;
    }

    public void getSynopsis(String urlToLoadSynopsis,
                            final IDataLoadingCallback<AirSynopsis> synopsisIDataLoadingCallback) {
        mISynopsesRestApi.getDescription(urlToLoadSynopsis).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<AirSynopsis>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        synopsisIDataLoadingCallback.onFailure(e);
                    }

                    @Override
                    public void onNext(AirSynopsis airSynopsis) {
                        synopsisIDataLoadingCallback.onResult(airSynopsis);
                    }
                });
    }
}
