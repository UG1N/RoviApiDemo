package com.demo.rovi.roviapidemo.model;

import com.google.gson.annotations.SerializedName;

public class References {

    public static String NOT_VALID_ID = "-1";

    @SerializedName("id")
    private String mId;

    @SerializedName("me")
    private String mMe;

    public References() {
    }

    public String getId() {
        return mId;
    }

    public String getMe() {
        return mMe;
    }

    @Override
    public String toString() {
        return "References{" +
                "mId=" + mId +
                ", mMe='" + mMe + '\'' +
                '}';
    }
}
