package com.demo.rovi.roviapidemo.model.dao;

import com.demo.rovi.roviapidemo.model.IDataLoadingCallback;
import com.demo.rovi.roviapidemo.model.TvChannels.Channel;
import com.demo.rovi.roviapidemo.model.TvChannels.TvChannels;
import com.demo.rovi.roviapidemo.model.restapi.IChannelsRestApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Alexey Kovalev
 * @since 28.03.2016.
 */
public final class ChannelsDao {

    private final IChannelsRestApi channelsRestApi;

    public ChannelsDao(IChannelsRestApi channelsRestApi) {
        this.channelsRestApi = channelsRestApi;
    }

    void getChannels(String urlToLoadChannels, final IDataLoadingCallback<Channel> channelsLoadingCallback) {
        channelsRestApi.getChannelsDataFromUrl(urlToLoadChannels).enqueue(
                new Callback<TvChannels>() {
                    @Override
                    public void onResponse(Call<TvChannels> call, Response<TvChannels> response) {
                        if (response.isSuccessful()) {
//                            channelsLoadingCallback.onResult(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<TvChannels> call, Throwable t) {
                        // TODO: 28.03.2016
                    }
                }
        );
    }


}
