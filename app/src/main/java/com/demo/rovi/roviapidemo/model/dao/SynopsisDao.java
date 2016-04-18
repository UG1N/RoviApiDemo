package com.demo.rovi.roviapidemo.model.dao;

import com.demo.rovi.roviapidemo.model.restapi.IDataLoadingCallback;
import com.demo.rovi.roviapidemo.model.restapi.ISynopsesRestApi;
import com.demo.rovi.roviapidemo.model.synopses.AirSynopsis;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SynopsisDao {

    private final ISynopsesRestApi mISynopsesRestApi;

    public SynopsisDao(ISynopsesRestApi iSynopsesRestApi) {
        mISynopsesRestApi = iSynopsesRestApi;
    }

    public void getSynopsis(String urlToLoadSynopsis,
                            final IDataLoadingCallback<AirSynopsis> synopsisIDataLoadingCallback) {
        mISynopsesRestApi.getDescription(urlToLoadSynopsis).enqueue(
                new Callback<AirSynopsis>() {
                    @Override
                    public void onResponse(Response<AirSynopsis> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            synopsisIDataLoadingCallback.onResult(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        synopsisIDataLoadingCallback.onFailure(t);
                    }
                });
    }
}
