package com.demo.rovi.roviapidemo.model.dao;

import com.demo.rovi.roviapidemo.model.restapi.ISynopsesRestApi;
import com.demo.rovi.roviapidemo.model.synopses.AirSynopsis;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SynopsisDao {

    private final ISynopsesRestApi mISynopsesRestApi;

    public SynopsisDao(ISynopsesRestApi iSynopsesRestApi) {
        mISynopsesRestApi = iSynopsesRestApi;
    }

    public Observable<AirSynopsis> getSynopsis(String urlToLoadSynopsis) {
        return mISynopsesRestApi.getDescription(urlToLoadSynopsis);
    }
}
