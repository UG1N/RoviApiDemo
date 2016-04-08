package com.demo.rovi.roviapidemo.model.channels;

import com.demo.rovi.roviapidemo.model.References;
import com.google.gson.annotations.SerializedName;

public class Logo {

    @SerializedName("ref")
    private References mLogoReferences;

    public References getLogoReferences() {
        return mLogoReferences;
    }

    @Override
    public String toString() {
        return "Logo{" +
                "mLogoReferences=" + mLogoReferences +
                '}';
    }
}
