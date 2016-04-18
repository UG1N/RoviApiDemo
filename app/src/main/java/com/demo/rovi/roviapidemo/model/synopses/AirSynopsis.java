package com.demo.rovi.roviapidemo.model.synopses;

import com.google.gson.annotations.SerializedName;

public class AirSynopsis {
    @SerializedName("synopsis")
    private BestSynopsis mAirSynopsis;

    public BestSynopsis getAirSynopsis() {
        return mAirSynopsis;
    }

    @Override
    public String toString() {
        return "AirSynopsis{" +
                "mAirSynopsis=" + mAirSynopsis +
                '}';
    }
}
