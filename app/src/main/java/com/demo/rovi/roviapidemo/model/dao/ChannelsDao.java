package com.demo.rovi.roviapidemo.model.dao;

import com.demo.rovi.roviapidemo.model.channels.TvChannels;
import com.demo.rovi.roviapidemo.model.restapi.IChannelsRestApi;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Alexey Kovalev
 * @since 28.03.2016.
 */
public final class ChannelsDao {

    private final IChannelsRestApi channelsRestApi;

    @Inject
    public ChannelsDao(IChannelsRestApi channelsRestApi) {
        this.channelsRestApi = channelsRestApi;
    }

    public Observable<TvChannels> getChannels(String urlToLoadChannels) {
        return channelsRestApi.getChannelsDataFromUrl(urlToLoadChannels);
    }
}
