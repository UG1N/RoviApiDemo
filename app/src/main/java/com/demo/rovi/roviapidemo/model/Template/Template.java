package com.demo.rovi.roviapidemo.model.Template;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Template implements Serializable {

    @SerializedName("data_airing_synopses")
    private DataAiring mDataAiring;
    @SerializedName("data_service_channels")
    private ServiceChannels mServiceChannels;
    @SerializedName("data_service_schedules")
    private ServiceSchedule mServiceSchedule;

    public DataAiring getDataAiring() {
        return mDataAiring;
    }

    public ServiceChannels getServiceChannels() {
        return mServiceChannels;
    }

    public ServiceSchedule getServiceSchedule() {
        return mServiceSchedule;
    }

    @Override
    public String toString() {
        return "TemplateFile{" +
                "mDataAiring='" + mDataAiring + '\'' +
                ", mServiceChannels='" + mServiceChannels + '\'' +
                ", mServiceSchedule='" + mServiceSchedule + '\'' +
                '}';
    }


}
