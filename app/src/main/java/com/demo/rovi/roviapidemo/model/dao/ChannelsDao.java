package com.demo.rovi.roviapidemo.model.dao;

import android.util.Log;

import com.demo.rovi.roviapidemo.model.restapi.IDataLoadingCallback;
import com.demo.rovi.roviapidemo.model.TvChannels.TvChannels;
import com.demo.rovi.roviapidemo.model.restapi.IChannelsRestApi;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
        channelsRestApi.getChannelsDataFromUrl(urlToLoadChannels).enqueue(
                new Callback<TvChannels>() {
                    @Override
                    public void onResponse(Response<TvChannels> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            channelsLoadingCallback.onResult(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // TODO: 28.03.2016
                        channelsLoadingCallback.onFailure(t);
                    }
                }
        );
    }
}
