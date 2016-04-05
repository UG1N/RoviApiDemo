package com.demo.rovi.roviapidemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.demo.rovi.roviapidemo.frragment.AiringFragment;
import com.demo.rovi.roviapidemo.model.internal.SimpleAiringObject;

import java.util.ArrayList;
import java.util.List;

public class AiringPageAdapter extends FragmentStatePagerAdapter {

    public static final String TAG = "AiringPageAdapter";
    private final int mFragmentCount = 3;
    private String[] mAiringTitle;
    private List<SimpleAiringObject> mSimpleAiringObjectList;

    public CharSequence getTitleForCurrentAir(int position) {
        return mAiringTitle[position];
    }

    //TODO: replace ENUM
    enum Broadcast {

        PREVIOUS("Previous Air"),
        NOW("Now"),
        NEXT("Next On Air");

        private String value;

        Broadcast(String value) {
            this.value = value;
        }

    }

    public AiringPageAdapter(FragmentManager fm) {
        super(fm);
        mAiringTitle = new String[mFragmentCount];
        mSimpleAiringObjectList = new ArrayList<>();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AiringFragment.newInstance(mAiringTitle[position]);
            case 1:
                return AiringFragment.newInstance(mAiringTitle[position]);
            case 2:
                return AiringFragment.newInstance(mAiringTitle[position]);
        }
        return null;
    }

    @Override
    public int getCount() {
        return mFragmentCount;
    }

    //    this updates the adapter
    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Broadcast.values()[position].value;
    }

    public void updateAirPage(String[] airIds) {
        for (int i = 0; i < airIds.length; i++) {
            mAiringTitle[i] = airIds[i];
        }
        notifyDataSetChanged();
    }

    public void updateAirPage2(List<SimpleAiringObject> airList) {
        mSimpleAiringObjectList = airList;
        notifyDataSetChanged();
    }

    public SimpleAiringObject callFromActivity(int position) {
        return mSimpleAiringObjectList.get(position);
    }
}