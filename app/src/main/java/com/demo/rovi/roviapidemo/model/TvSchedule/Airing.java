package com.demo.rovi.roviapidemo.model.TvSchedule;

import com.demo.rovi.roviapidemo.model.References;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Airing {
    @SerializedName("ref")
    private References mAiringReferences;
    @SerializedName("title")
    private String mAiringTitle;
    @SerializedName("image")
    private MediaImage mMediaImage;
    @SerializedName("start")
    private Date mStartAir;
    @SerializedName("end")
    private Date mEndAir;

    public References getAiringReferences() {
        return mAiringReferences;
    }

    public String getAiringTitle() {
        return mAiringTitle;
    }

    public MediaImage getMediaImage() {
        return mMediaImage;
    }

    public Date getStartAir() {
        return mStartAir;
    }

    public Date getEndAir() {
        return mEndAir;
    }

    @Override
    public String toString() {
        return "Airing{" +
                "mAiringReferences=" + mAiringReferences +
                ", mAiringTitle='" + mAiringTitle + '\'' +
                ", mMediaImage=" + mMediaImage +
                ", mStartAir=" + mStartAir +
                ", mEndAir=" + mEndAir +
                '}' +
                "\n";
    }
}
