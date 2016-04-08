package com.demo.rovi.roviapidemo.model.schedule;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Schedule {
    @SerializedName("airings")
    private Airing[] mAirings;

    public Airing[] getAirings() {
        return mAirings;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "mAirings=" + Arrays.toString(mAirings) +
                '}';
    }
}
