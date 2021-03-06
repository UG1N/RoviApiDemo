package com.demo.rovi.roviapidemo.model.channels;

import com.google.gson.annotations.SerializedName;

public class WindowChannel {
    @SerializedName("source")
    private ChannelDataSource mDataSources;

    public ChannelDataSource getDataSources() {
        return mDataSources;
    }

    @Override
    public String toString() {
        return "WindowChannel{" +
                "mDataSources=" + mDataSources +
                '}' + '\n';
    }
}
