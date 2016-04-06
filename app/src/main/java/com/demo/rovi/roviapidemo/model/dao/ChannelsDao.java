package com.demo.rovi.roviapidemo.model.dao;

import com.demo.rovi.roviapidemo.model.TvChannels.TvChannels;
import com.demo.rovi.roviapidemo.model.restapi.IChannelsRestApi;
import com.demo.rovi.roviapidemo.model.restapi.IDataLoadingCallback;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Alexey Kovalev
 * @since 28.03.2016.
 */
public final class ChannelsDao {

    private final IChannelsRestApi channelsRestApi;

    public ChannelsDao(IChannelsRestApi channelsRestApi) {
        this.channelsRestApi = channelsRestApi;
    }


    public void getChannels(String urlToLoadChannels, final IDataLoadingCallback<TvChannels> channelsLoadingCallback) {
        channelsRestApi.getChannelsDataFromUrl(urlToLoadChannels).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<TvChannels>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        channelsLoadingCallback.onFailure(e);
                    }

                    @Override
                    public void onNext(TvChannels tvChannels) {
                        channelsLoadingCallback.onResult(tvChannels);
                    }
                });
    }
}
