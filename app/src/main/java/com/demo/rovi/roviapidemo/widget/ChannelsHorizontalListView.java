package com.demo.rovi.roviapidemo.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.demo.rovi.roviapidemo.adapter.ChannelListAdapter;
import com.demo.rovi.roviapidemo.model.channels.Channel;

import java.util.Collections;
import java.util.List;

public final class ChannelsHorizontalListView extends RecyclerView {

    public interface ChannelItemSelectionListener {
        // TODO: 31.03.2016 pass entity instead of index
        void onChannelClick(int channelClickedPosition);
    }

    public interface OnListEndNotificationListener {
        void onListEnd(int lastCompletelyVisibleItemPosition);
    }

    private final OnScrollListener listEndScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            final int lastCompletelyVisibleItemPosition = getLayoutManager().findLastCompletelyVisibleItemPosition();
            boolean isLastViewDisplayed = lastCompletelyVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1;

            if (newState == RecyclerView.SCROLL_STATE_IDLE && isLastViewDisplayed) {
                fireOnListEnd(lastCompletelyVisibleItemPosition);
            }
        }
    };

    private OnListEndNotificationListener mOnListEndNotificationListener;
    private ChannelItemSelectionListener mChannelItemSelectionListenerExternal;

    public ChannelsHorizontalListView(Context context) {
        this(context, null);
    }

    public ChannelsHorizontalListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChannelsHorizontalListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        addOnScrollListener(listEndScrollListener);

        setAdapter(new ChannelListAdapter(Collections.<Channel>emptyList(), new ChannelItemSelectionListener() {
            @Override
            public void onChannelClick(int channelClickedPosition) {
                if (mChannelItemSelectionListenerExternal != null) {
                    mChannelItemSelectionListenerExternal.onChannelClick(channelClickedPosition);
                }
            }
        }));
    }

    public void setOnListEndNotificationListener(OnListEndNotificationListener listEndNotificationListener) {
        mOnListEndNotificationListener = listEndNotificationListener;
    }

    public void setOnChannelSelectionListener(ChannelItemSelectionListener channelSelectionListener) {
        mChannelItemSelectionListenerExternal = channelSelectionListener;
    }

    private void fireOnListEnd(int lastCompletelyVisibleItemPosition) {
        if (mOnListEndNotificationListener != null) {
            mOnListEndNotificationListener.onListEnd(lastCompletelyVisibleItemPosition);
        }
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return (LinearLayoutManager) super.getLayoutManager();
    }

    @Override
    public ChannelListAdapter getAdapter() {
        return (ChannelListAdapter) super.getAdapter();
    }

    public void appendData(List<Channel> channels) {
        getAdapter().addNewChannels(channels);
    }

}
