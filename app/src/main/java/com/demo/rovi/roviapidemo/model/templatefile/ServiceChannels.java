package com.demo.rovi.roviapidemo.model.templatefile;

import com.google.gson.annotations.SerializedName;

public class ServiceChannels {
    @SerializedName("screen")
    private String mChannels;

    public String getChannels() {
        return mChannels;
    }

    @Override
    public String toString() {
        return "ServiceChannels{" +
                "mChannels='" + mChannels + '\'' +
                '}';
    }
}
