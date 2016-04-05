package com.demo.rovi.roviapidemo.model.Template;

import com.google.gson.annotations.SerializedName;

public class ServiceSchedule {
    @SerializedName("single")
    private String mSingleChannelSchedule;

    public String getSingleChannelSchedule() {
        return mSingleChannelSchedule;
    }

    @Override
    public String toString() {
        return "ServiceSchedule{" +
                "mSingleChannelSchedule='" + mSingleChannelSchedule + '\'' +
                '}';
    }
}