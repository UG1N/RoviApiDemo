package com.demo.rovi.roviapidemo.model.schedule;

import com.demo.rovi.roviapidemo.model.References;
import com.google.gson.annotations.SerializedName;

public class MediaImage {
    @SerializedName("ref")
    private References mMediaImageReferences;

    public References getMediaImageReferences() {
        return mMediaImageReferences;
    }

    @Override
    public String toString() {
        return "MediaImage{" +
                "mMediaImageReferences=" + mMediaImageReferences +
                '}';
    }
}
