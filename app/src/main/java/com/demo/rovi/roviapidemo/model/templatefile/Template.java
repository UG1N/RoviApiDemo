package com.demo.rovi.roviapidemo.model.templatefile;

import com.google.gson.annotations.SerializedName;

public class Template {

    @SerializedName("data_airing_synopses")
    private DataAiring mDataAiring;

    @SerializedName("data_service_channels")
    private ServiceChannels mServiceChannels;

    @SerializedName("data_service_schedules")
    private ServiceSchedule mServiceSchedule;

    @SerializedName("media_image")
    private AirImage mAirImage;

    @SerializedName("media_logo")
    private String mMediaLogo;

    public DataAiring getDataAiring() {
        return mDataAiring;
    }

    public AirImage getAirImage() {
        return mAirImage;
    }

    public ServiceChannels getServiceChannels() {
        return mServiceChannels;
    }

    public ServiceSchedule getServiceSchedule() {
        return mServiceSchedule;
    }

    public String getMediaLogo() {
        return mMediaLogo;
    }

    @Override
    public String toString() {
        return "Template{" +
                "mDataAiring=" + mDataAiring + '\n' +
                ", mServiceChannels=" + mServiceChannels + '\n' +
                ", mServiceSchedule=" + mServiceSchedule + '\n' +
                ", mAirImage=" + mAirImage + '\n' +
                ", mMediaLogo='" + mMediaLogo + '\'' +
                '}';
    }
}
