package com.demo.rovi.roviapidemo.model.channels;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class TvChannels {

    @SerializedName("page")
    private int mPageNumber;

    @SerializedName("size")
    private int mPageSize;

    @SerializedName("total")
    private int mTotalChannels;

    @SerializedName("channels")
    private List<Channel> mChannels;

    public TvChannels() {
    }

    public int getPageNumber() {
        return mPageNumber;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public int getTotalChannels() {
        return mTotalChannels;
    }

    public List<Channel> getChannels() {
        return mChannels;
    }

    /*public Channel getChannel(int channelNumber) {
        return mChannels[channelNumber];
    }

    public void setChannels(Channel[] channels) {
        mChannels = channels;
    }*/

    @Override
    public String toString() {
        return "TvChannels{" +
                "mPageNumber=" + mPageNumber +
                ", mPageSize=" + mPageSize +
                ", mTotalChannels=" + mTotalChannels +
                ", mChannels=" + mChannels +
                '}';
    }
}
