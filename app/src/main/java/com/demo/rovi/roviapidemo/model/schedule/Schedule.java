package com.demo.rovi.roviapidemo.model.schedule;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class Schedule {
    @SerializedName("airings")
    private List<Airing> mAirings;

    public List<Airing> getAirings() {
        return mAirings;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "mAirings=" + mAirings +
                '}';
    }
}
