package com.demo.rovi.roviapidemo.model.Template;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataAiring implements Serializable {
    @SerializedName("collection")
    private String mSynopsesColection;

    public String getSynopsesColection() {
        return mSynopsesColection;
    }

    @Override
    public String toString() {
        return "DataAiring{" +
                "mSynopsesColection='" + mSynopsesColection + '\'' +
                '}';
    }
}
