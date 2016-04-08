package com.demo.rovi.roviapidemo.model.templatefile;

import com.google.gson.annotations.SerializedName;

public class AirImage {

    @SerializedName("sized")
    private String mAirMediaImageUrl;

    public String getAirMediaImageUrl() {
        return mAirMediaImageUrl;
    }
}
