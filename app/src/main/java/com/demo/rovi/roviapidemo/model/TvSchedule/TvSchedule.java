package com.demo.rovi.roviapidemo.model.TvSchedule;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class TvSchedule {
    @SerializedName("schedule")
    private Schedule[] mScheduleForSingleChannel;

    public Schedule[] getScheduleForSingleChannel() {
        return mScheduleForSingleChannel;
    }

    @Override
    public String toString() {
        return "TvSchedule{" +
                "mScheduleForSingleChannel=" + Arrays.toString(mScheduleForSingleChannel) +
                '}';
    }
}
