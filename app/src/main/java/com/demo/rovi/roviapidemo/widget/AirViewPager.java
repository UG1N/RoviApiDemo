package com.demo.rovi.roviapidemo.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

import com.demo.rovi.roviapidemo.activity.ChannelListActivity;
import com.demo.rovi.roviapidemo.adapter.AiringPageAdapter;
import com.demo.rovi.roviapidemo.model.internal.SimpleAiringObject;

import java.util.List;

public class AirViewPager extends ViewPager {

    private final String TAG = getClass().getSimpleName();

    public interface PageOnFocusListener {
        void onPageInFocus(int selectedChannel);
    }

    private PageOnFocusListener mPageOnFocusListener;

    private final OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            pageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public AirViewPager(Context context) {
        this(context, null);
    }

    public AirViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
//        TODO: (ChannelListActivity) is bad idea
        setAdapter(new AiringPageAdapter(((ChannelListActivity) context).getSupportFragmentManager()));
        addOnPageChangeListener(mOnPageChangeListener);
    }

    public void setPageOnFocusListener(PageOnFocusListener pageOnFocusListener) {
        mPageOnFocusListener = pageOnFocusListener;
    }

    private void pageSelected(int pagePosition) {
        if (mPageOnFocusListener != null) {
            mPageOnFocusListener.onPageInFocus(pagePosition);
        }
    }

    @Override
    public AiringPageAdapter getAdapter() {
        return (AiringPageAdapter) super.getAdapter();
    }

    public CharSequence getTitle(int byPosition) {
        Log.e(TAG, "getTitle: " + getAdapter().getTitleForCurrentAir(byPosition));
        return getAdapter().getTitleForCurrentAir(byPosition);
    }

    public String getSynopsisId(int byPosition) {
        Log.e(TAG, "getSynopsisId: " + getAdapter().getSynopsisId(byPosition));
        return getAdapter().getSynopsisId(byPosition);
    }

    public void update(List<SimpleAiringObject> simpleAiringObjects) {
        Log.e(TAG, "update: " + (simpleAiringObjects.size()));
        getAdapter().updateAirPage(simpleAiringObjects);
    }


}
