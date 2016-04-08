package com.demo.rovi.roviapidemo.model.templatefile;

import com.google.gson.annotations.SerializedName;

public class DataAiring {
    @SerializedName("best")
    private String mSynopsisBest;

    public String getSynopsisBest() {
        return mSynopsisBest;
    }

    @Override
    public String toString() {
        return "DataAiring{" +
                "mSynopsisBest='" + mSynopsisBest + '\'' +
                '}';
    }
}
