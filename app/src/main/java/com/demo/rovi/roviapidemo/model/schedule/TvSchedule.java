package com.demo.rovi.roviapidemo.model.schedule;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvSchedule {
    @SerializedName("schedule")
    private List<Schedule> mScheduleForSingleChannel;

    private List<Schedule> getScheduleForSingleChannel() {
        return mScheduleForSingleChannel;
    }

    public Schedule getScheduleForChannel() {
        return mScheduleForSingleChannel.get(0);
    }

    @Override
    public String toString() {
        return "TvSchedule{" +
                "mScheduleForSingleChannel=" + mScheduleForSingleChannel.get(0) +
                '}';
    }
}
