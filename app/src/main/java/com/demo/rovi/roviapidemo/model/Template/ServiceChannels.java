package com.demo.rovi.roviapidemo.model.Template;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServiceChannels implements Serializable {
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
