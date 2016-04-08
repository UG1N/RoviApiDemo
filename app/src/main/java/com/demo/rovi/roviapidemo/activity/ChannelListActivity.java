package com.demo.rovi.roviapidemo.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.damnhandy.uri.template.UriTemplate;
import com.demo.rovi.roviapidemo.R;
import com.demo.rovi.roviapidemo.adapter.AiringPageAdapter;
import com.demo.rovi.roviapidemo.adapter.ChannelListAdapter;
import com.demo.rovi.roviapidemo.application.RoviApplication;
import com.demo.rovi.roviapidemo.model.BackendConstants;
import com.demo.rovi.roviapidemo.model.channels.Channel;
import com.demo.rovi.roviapidemo.model.channels.TvChannels;
import com.demo.rovi.roviapidemo.model.dao.ChannelsDao;
import com.demo.rovi.roviapidemo.model.dao.ScheduleDao;
import com.demo.rovi.roviapidemo.model.dao.SynopsisDao;
import com.demo.rovi.roviapidemo.model.internal.AbstractRxSubscriber;
import com.demo.rovi.roviapidemo.model.internal.SimpleAiringObject;
import com.demo.rovi.roviapidemo.model.restapi.IChannelsRestApi;
import com.demo.rovi.roviapidemo.model.restapi.IDataLoadingCallback;
import com.demo.rovi.roviapidemo.model.restapi.IScheduleRestApi;
import com.demo.rovi.roviapidemo.model.restapi.ISynopsesRestApi;
import com.demo.rovi.roviapidemo.model.schedule.Schedule;
import com.demo.rovi.roviapidemo.model.schedule.TvSchedule;
import com.demo.rovi.roviapidemo.model.synopses.AirSynopsis;
import com.demo.rovi.roviapidemo.model.templatefile.TemplateFile;
import com.demo.rovi.roviapidemo.widget.ChannelsHorizontalListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ChannelListActivity extends AppCompatActivity {

    private static final String TAG = ChannelListActivity.class.getSimpleName();
    public static final int PAGE_COUNT = 3;

    private TemplateFile mTemplateFile;
    private ChannelsDao mChannelsDao;
    private ScheduleDao mScheduleDao;
    private SynopsisDao mSynopsisDao;

    @Deprecated
    private TvChannels mTvChannels;

    @Deprecated
    private List<SimpleAiringObject> mSimpleAiringObjectList;


    private AiringPageAdapter mAiringPageAdapter;

    private int lastlyRequestedLoadingForChannelPosition;
    private int currentlySelectedChannelPosition;


    @Bind(R.id.recycler_view)
    ChannelsHorizontalListView mChannelsListView;

    @Bind(R.id.airing_page)
    ViewPager mViewPager;

    @Bind(R.id.airing_title)
    TextView mAiringTitleTextView;

    @Bind(R.id.airing_description)
    TextView mAiringDescriptionTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDaos();

        setContentView(R.layout.channel_list_activity);
        if (savedInstanceState == null) {
            currentlySelectedChannelPosition = 1;
        }

        ButterKnife.bind(this);

        mTemplateFile = RoviApplication.getInstance().getTemplateFile();
        mSimpleAiringObjectList = new ArrayList<>();

        initAiringsPager();
        initChannelsListView();

        loadInitialChannelsChunk();
    }

    private void initChannelsListView() {
        mChannelsListView.setOnListEndNotificationListener(new ChannelsHorizontalListView.OnListEndNotificationListener() {
            @Override
            public void onListEnd(int lastCompletelyVisibleItemPosition) {
                final boolean isLastPage = mTvChannels.getTotalChannels() == lastCompletelyVisibleItemPosition + 1;
                if (!isLastPage) {
                    triggerLoadAdditionalChannelItems(lastCompletelyVisibleItemPosition);
                }
            }
        });

        mChannelsListView.setOnChannelSelectionListener(new ChannelsHorizontalListView.ChannelItemSelectionListener() {
            @Override
            public void onChannelClick(int channelClickedPosition) {
                currentlySelectedChannelPosition = channelClickedPosition;
                loadScheduleDataForChannel(channelClickedPosition);
            }
        });
    }

    private void initAiringsPager() {
        mAiringPageAdapter = new AiringPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAiringPageAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("PageAdapter", "onCreate: ");
                if (mSimpleAiringObjectList.get(position) != null) {
                    bindBottomDescriptionContainer(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void bindBottomDescriptionContainer(int position) {
        mAiringTitleTextView.setText(mAiringPageAdapter.getTitleForCurrentAir(position));
        loadDescriptionOfSelectedAir(mAiringPageAdapter.getSynopsisId(position));
    }

    private void initDaos() {
        mChannelsDao = new ChannelsDao(RoviApplication.createRestApiServiceImpl(IChannelsRestApi.class));
        mScheduleDao = new ScheduleDao(RoviApplication.createRestApiServiceImpl(IScheduleRestApi.class));
    }

    private Observable<TvChannels> loadChannelsDataForPage(int pageNumber) {
        String channelsLoadingUrl = UriTemplate
                .fromTemplate(mTemplateFile.getTemplate().getServiceChannels().getChannels())
                .set("id", BackendConstants.TELEVISION_SERVICE)
                .set("page", pageNumber)
                .expand();
        return mChannelsDao.getChannels(channelsLoadingUrl);
    }

    private Observable<TvSchedule> loadScheduleDataForChannel(final int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String schedulesLoadingUrl = UriTemplate
                .fromTemplate(mTemplateFile.getTemplate().getServiceSchedule().getSingleChannelSchedule())
                .set("id", BackendConstants.TELEVISION_SERVICE)
                .set("date", simpleDateFormat.format(new Date()))
                .set("page", position)
                .expand();

        return mScheduleDao.getSchedule(schedulesLoadingUrl);
    }

    private void updateAirPageAdapter(Schedule airSchedule) {
        mSimpleAiringObjectList.clear();
        int currentAirFromSchedule = 0;
        long currentTimeInMs = System.currentTimeMillis();

        for (int i = 0; i < airSchedule.getAirings().length; i++) {
            if (airSchedule.getAirings()[i].getStartAir().getTime() <= currentTimeInMs
                    && currentTimeInMs <= airSchedule.getAirings()[i].getEndAir().getTime()) {
                currentAirFromSchedule = i;
                break;
            }
        }
        // - deprecated, -customViewPager, smth with this method, synopsis + template
        for (int i = 0; i < PAGE_COUNT; i++) {
            if (airSchedule.getAirings().length == 1 && currentAirFromSchedule == 0) {
                getAirFromSchedule(airSchedule, currentAirFromSchedule);
            } else if (currentAirFromSchedule == 0 && i <= 1) {
                getAirFromSchedule(airSchedule, currentAirFromSchedule);
            } else if (currentAirFromSchedule == 0) {
                getAirFromSchedule(airSchedule, currentAirFromSchedule + 1);
            } else if (airSchedule.getAirings().length == currentAirFromSchedule + 1 && i > 1) {
                getAirFromSchedule(airSchedule, currentAirFromSchedule);
            } else {
                getAirFromSchedule(airSchedule, currentAirFromSchedule + i - 1);
            }
        }

        mAiringPageAdapter.updateAirPage(mSimpleAiringObjectList);

        mViewPager.setCurrentItem(1);
        bindBottomDescriptionContainer(1);
    }

    private void getAirFromSchedule(Schedule schedule, int numberOfAir) {
        String imageId;
        if (schedule.getAirings()[numberOfAir].getMediaImage() != null) {
            imageId = (String.valueOf(schedule.getAirings()[numberOfAir]
                    .getMediaImage().getMediaImageReferences().getId()));
        } else {
            imageId = (String.valueOf(-1));
        }
        String synopsisId = String.valueOf(schedule.getAirings()[numberOfAir]
                .getAiringReferences().getId());
        String title = schedule.getAirings()[numberOfAir].getAiringTitle();

        mSimpleAiringObjectList.add(new SimpleAiringObject(synopsisId, imageId, title));
    }

    private void loadDescriptionOfSelectedAir(final String urlId) {
        Log.e(TAG, "url id in if- > " + urlId);

        mSynopsisDao = new SynopsisDao(RoviApplication.createRestApiServiceImpl(ISynopsesRestApi.class));
        mSynopsisDao.getSynopsis(getUrlForAirSynopsis(urlId), new IDataLoadingCallback<AirSynopsis>() {
            @Override
            public void onResult(AirSynopsis loadedData) {
                Log.e(TAG, "onResult: AirSynopsis" + urlId + loadedData.getAirSynopsis());
                mAiringDescriptionTextView.setText(loadedData.getAirSynopsis().getSynopsis());
            }

            @Override
            public void onFailure(Throwable ex) {
                Log.e(TAG, "loadDescriptionOfSelectedAir onFailure: " + urlId, ex);
                mAiringDescriptionTextView.setText(R.string.no_description);
            }
        });
    }

    /**
     * Returns absolute url of "air synopsis" by id.
     * %2A replacing '*' (UriTemplate didn't encode Object/Data value)
     *
     * @param airId unique id of air
     * @return string url
     */
    private String getUrlForAirSynopsis(String airId) {
        return UriTemplate
                .fromTemplate(mTemplateFile.getTemplate().getDataAiring().getSynopsisBest())
                .set("id", airId)
                .set("length", "long")
                .set("length2", "short")
                .set("length3", "plain")
                .set("length4", "extended")
                .set("in", "en-US")
                .set("in2", "en-" + "%2A")
                .set("in3", "%2A")
                .expand();
    }

    private void loadInitialChannelsChunk() {
        loadChannelsDataForPage(1)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<TvChannels, Observable<TvSchedule>>() {
                    @Override
                    public Observable<TvSchedule> call(TvChannels tvChannels) {
                        mTvChannels = tvChannels;
                        mChannelsListView.appendData(tvChannels.getChannels());
                        return loadScheduleDataForChannel(currentlySelectedChannelPosition);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbstractRxSubscriber<TvSchedule>() {
                    @Override
                    public void onNext(TvSchedule tvSchedule) {
                        updateAirPageAdapter(tvSchedule.getScheduleForChannel());
                    }
                });
    }

    private void triggerLoadAdditionalChannelItems(int lastCompletelyVisibleItemPosition) {
        if (lastlyRequestedLoadingForChannelPosition != lastCompletelyVisibleItemPosition) {
            Log.e(TAG, "triggerLoadAdditionalChannelItems");
            lastlyRequestedLoadingForChannelPosition = lastCompletelyVisibleItemPosition;

            loadChannelsDataForPage(mTvChannels.getPageNumber() + 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new AbstractRxSubscriber<TvChannels>() {
                        @Override
                        public void onNext(TvChannels loadedData) {
                            mTvChannels = loadedData;
                            mChannelsListView.appendData(loadedData.getChannels());
                            lastlyRequestedLoadingForChannelPosition = 0;
                        }
                    });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentlySelectedChannelPosition", currentlySelectedChannelPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            currentlySelectedChannelPosition = savedInstanceState.getInt("currentlySelectedChannelPosition");
        }
    }
}
