package com.demo.rovi.roviapidemo.model.TvChannels;

import com.demo.rovi.roviapidemo.model.References;
import com.google.gson.annotations.SerializedName;

public class ChannelDataSource {
    @SerializedName("ref")
    private References mSourceRef;
    @SerializedName("logo")
    private Logo mLogo;

    public References getSourceRef() {
        return mSourceRef;
    }

    public Logo getLogo() {
        return mLogo;
    }

    @Override
    public String toString() {
        return "ChannelDataSource{" +
                "mSourceRef=" + mSourceRef +
                ", mLogo=" + mLogo +
                '}';
    }
}
