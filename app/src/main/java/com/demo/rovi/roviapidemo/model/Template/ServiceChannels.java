package com.demo.rovi.roviapidemo.model.Template;

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
