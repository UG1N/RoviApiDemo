package com.demo.rovi.roviapidemo.model.TvChannels;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Channel {
    @SerializedName("channel")
    private int mChannelNumber;
    @SerializedName("windows")
    private WindowChannel[] mWindowChannels;

    public Channel(int channelNumber, WindowChannel[] windowChannels) {
        mChannelNumber = channelNumber;
        mWindowChannels = windowChannels;
    }

    public int getChannelNumber() {
        return mChannelNumber;
    }

    public WindowChannel[] getWindowChannels() {
        return mWindowChannels;
    }

    public void setChannelNumber(int channelNumber) {
        mChannelNumber = channelNumber;
    }

    public void setWindowChannels(WindowChannel[] windowChannels) {
        mWindowChannels = windowChannels;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "mChannelNumber=" + mChannelNumber +
                ", mWindowChannels=" +
                Arrays.toString(mWindowChannels) +
                '}' + "\n";
    }

}
