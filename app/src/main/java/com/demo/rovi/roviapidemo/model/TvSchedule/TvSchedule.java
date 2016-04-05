package com.demo.rovi.roviapidemo.model.TvSchedule;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class TvSchedule {
    @SerializedName("schedule")
    private Schedule[] mScheduleForSingleChannel;
    // Delete ?
    private Schedule[] getScheduleForSingleChannel() {
        return mScheduleForSingleChannel;
    }

    public Schedule getScheduleForChannel() {
        return mScheduleForSingleChannel[0];
    }

    @Override
    public String toString() {
        return "TvSchedule{" +
                "mScheduleForSingleChannel=" + Arrays.toString(mScheduleForSingleChannel) +
                '}';
    }
}
