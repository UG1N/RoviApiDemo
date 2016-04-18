package com.demo.rovi.roviapidemo.model.dao;

import com.demo.rovi.roviapidemo.model.restapi.ISynopsesRestApi;
import com.demo.rovi.roviapidemo.model.synopses.AirSynopsis;

import javax.inject.Inject;

import rx.Observable;

public class SynopsisDao {

    private final ISynopsesRestApi mISynopsesRestApi;

    @Inject
    public SynopsisDao(ISynopsesRestApi iSynopsesRestApi) {
        mISynopsesRestApi = iSynopsesRestApi;
    }

    public Observable<AirSynopsis> getSynopsis(String urlToLoadSynopsis) {
        return mISynopsesRestApi.getDescription(urlToLoadSynopsis);
    }
}
