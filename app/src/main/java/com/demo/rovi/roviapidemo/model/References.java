package com.demo.rovi.roviapidemo.model;

import com.google.gson.annotations.SerializedName;

public class References {
    @SerializedName("id")
    private long mId;
    @SerializedName("me")
    private String mMe;

    public References(int id, String me) {
        mId = id;
        mMe = me;
    }

    public long getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getMe() {
        return mMe;
    }

    public void setMe(String me) {
        mMe = me;
    }

    @Override
    public String toString() {
        return "References{" +
                "mId=" + mId +
                ", mMe='" + mMe + '\'' +
                '}';
    }
}
