package com.demo.rovi.roviapidemo.model.TvChannels;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class TvChannels {
    @SerializedName("page")
    private int mPageNumber;
    @SerializedName("size")
    private int mPageSize;
    @SerializedName("total")
    private int mTotalChannels;
    @SerializedName("channels")
    private Channel[] mChannels;

    public TvChannels(int pageNumber, int pageSize, int totalChannels, Channel[] channels) {
        mPageNumber = pageNumber;
        mPageSize = pageSize;
        mTotalChannels = totalChannels;
        mChannels = channels;
    }

    public int getPageNumber() {
        return mPageNumber;
    }

    public void setPageNumber(int pageNumber) {
        mPageNumber = pageNumber;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }

    public int getTotalChannels() {
        return mTotalChannels;
    }

    public void setTotalChannels(int totalChannels) {
        mTotalChannels = totalChannels;
    }

    public Channel[] getChannels() {
        return mChannels;
    }

    public Channel getChannel(int channelNumber) {
        return mChannels[channelNumber];
    }

    public void setChannels(Channel[] channels) {
        mChannels = channels;
    }

    @Override
    public String toString() {
        return "TvChannels{" +
                "mPageNumber=" + mPageNumber +
                ", mPageSize=" + mPageSize +
                ", mTotalChannels=" + mTotalChannels +
                ", mChannels=" + Arrays.toString(mChannels) +
                '}';
    }
}
