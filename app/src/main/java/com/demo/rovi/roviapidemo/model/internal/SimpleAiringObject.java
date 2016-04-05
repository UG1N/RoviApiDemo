package com.demo.rovi.roviapidemo.model.internal;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleAiringObject implements Parcelable {
    private String mSynopsisId;
    private String mImageIconId;
    private String mTitle;

    public SimpleAiringObject() {
    }

    public SimpleAiringObject(String synopsisId, String imageIconId, String title) {
        mSynopsisId = synopsisId;
        mImageIconId = imageIconId;
        mTitle = title;
    }

    public String getSynopsisId() {
        return mSynopsisId;
    }

    public void setSynopsisId(String synopsisId) {
        mSynopsisId = synopsisId;
    }

    public String getImageIconId() {
        return mImageIconId;
    }

    public void setImageIconId(String imageIconId) {
        mImageIconId = imageIconId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public String toString() {
        return "SimpleAiringObject{" +
                "mSynopsisId='" + mSynopsisId + '\'' +
                ", mImageIconId='" + mImageIconId + '\'' +
                ", mTitle='" + mTitle + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSynopsisId);
        dest.writeString(mImageIconId);
        dest.writeString(mTitle);
    }

    private SimpleAiringObject(Parcel in) {
        mSynopsisId = in.readString();
        mImageIconId = in.readString();
        mTitle = in.readString();
    }

    public static final Creator<SimpleAiringObject> CREATOR = new Creator<SimpleAiringObject>() {
        @Override
        public SimpleAiringObject createFromParcel(Parcel in) {
            return new SimpleAiringObject(in);
        }

        @Override
        public SimpleAiringObject[] newArray(int size) {
            return new SimpleAiringObject[size];
        }
    };
}
