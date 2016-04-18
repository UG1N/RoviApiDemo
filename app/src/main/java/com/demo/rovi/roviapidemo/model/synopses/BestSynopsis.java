package com.demo.rovi.roviapidemo.model.synopses;

import com.google.gson.annotations.SerializedName;

public class BestSynopsis {
    @SerializedName("synopsis")
    private String mSynopsis;

    public String getSynopsis() {
        return mSynopsis;
    }

    @Override
    public String toString() {
        return "BestSynopsis{" +
                "mSynopsis='" + mSynopsis + '\'' +
                '}';
    }
}
