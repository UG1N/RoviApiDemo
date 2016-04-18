package com.demo.rovi.roviapidemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.damnhandy.uri.template.UriTemplate;
import com.demo.rovi.roviapidemo.R;
import com.demo.rovi.roviapidemo.application.RoviApplication;
import com.demo.rovi.roviapidemo.model.BackendConstants;
import com.demo.rovi.roviapidemo.model.channels.TvChannels;
import com.demo.rovi.roviapidemo.model.dao.ChannelsDao;
import com.demo.rovi.roviapidemo.model.dao.ScheduleDao;
import com.demo.rovi.roviapidemo.model.dao.SynopsisDao;
import com.demo.rovi.roviapidemo.model.internal.AbstractRxSubscriber;
import com.demo.rovi.roviapidemo.model.internal.SimpleAiringObject;
import com.demo.rovi.roviapidemo.model.schedule.Airing;
import com.demo.rovi.roviapidemo.model.schedule.Schedule;
import com.demo.rovi.roviapidemo.model.schedule.TvSchedule;
import com.demo.rovi.roviapidemo.model.synopses.AirSynopsis;
import com.demo.rovi.roviapidemo.model.templatefile.TemplateFile;
import com.demo.rovi.roviapidemo.widget.AirViewPager;
import com.demo.rovi.roviapidemo.widget.ChannelsHorizontalListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ChannelListActivity extends AppCompatActivity {

    private static final String TAG = ChannelListActivity.class.getSimpleName();
    public static final int PAGE_COUNT = 3;

    private static final int NOT_DEFINED_AIRING_POSITION = -1;

    private TemplateFile mTemplateFile;

    private int mTotalChannelsAmount;
    private int mCurrentTenSelectedChannels;
    private int mLastlyRequestedLoadingForChannelPosition;
    private int mCurrentlySelectedChannelPosition;

    @Bind(R.id.recycler_view)
    ChannelsHorizontalListView mChannelsListView;

    @Bind(R.id.airing_page)
    AirViewPager mAirViewPager;

    @Bind(R.id.airing_title)
    TextView mAiringTitleTextView;

    @Bind(R.id.airing_description)
    TextView mAiringDescriptionTextView;

    @Inject
    ChannelsDao mChannelsDao;

    @Inject
    ScheduleDao mScheduleDao;

    @Inject
    SynopsisDao mSynopsisDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
        initDaos();

        setContentView(R.layout.channel_list_activity);
        if (savedInstanceState == null) {
            mCurrentlySelectedChannelPosition = 1;
        }
        ButterKnife.bind(this);
        RoviApplication.getApplicationInjector().inject(this);


        initAiringsPager();
        loadInitialChannelsChunk();
        initChannelsListView();
    }

    private void initDaos() {
        mTemplateFile = RoviApplication.getInstance().getTemplateFile();
        RoviApplication.getApplicationInjector().inject(this);
    }

    private void initAiringsPager() {
        mAirViewPager.setPageOnFocusListener(new AirViewPager.PageOnFocusListener() {
            @Override
            public void onPageInFocus(int selectedChannel) {
                bindBottomDescriptionContainer(selectedChannel);
            }
        });
    }

    private void loadInitialChannelsChunk() {
        loadChannelsDataForPage(1)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<TvChannels, Observable<TvSchedule>>() {
                    @Override
                    public Observable<TvSchedule> call(TvChannels tvChannels) {
                        mCurrentTenSelectedChannels = tvChannels.getPageNumber();
                        mTotalChannelsAmount = tvChannels.getTotalChannels();
                        mChannelsListView.appendData(tvChannels.getChannels());
                        return loadScheduleDataForChannel(mCurrentlySelectedChannelPosition);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbstractRxSubscriber<TvSchedule>() {
                    @Override
                    public void onNext(TvSchedule tvSchedule) {
                        updateAirPageAdapterAndDescriptionContainer(tvSchedule.getScheduleForChannel());
                    }
                });
    }

    private void initChannelsListView() {
        mChannelsListView.setOnListEndNotificationListener(new ChannelsHorizontalListView.OnListEndNotificationListener() {
            @Override
            public void onListEnd(int lastCompletelyVisibleItemPosition) {
                final boolean isLastPage = mTotalChannelsAmount == lastCompletelyVisibleItemPosition + 1;
                if (!isLastPage) {
                    triggerLoadAdditionalChannelItems(lastCompletelyVisibleItemPosition);
                }
            }
        });

        mChannelsListView.setOnChannelSelectionListener(new ChannelsHorizontalListView.ChannelItemSelectionListener() {
            @Override
            public void onChannelClick(int channelClickedPosition) {
                mCurrentlySelectedChannelPosition = channelClickedPosition;
                loadScheduleDataForChannel(channelClickedPosition)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new AbstractRxSubscriber<TvSchedule>() {
                            @Override
                            public void onNext(TvSchedule tvSchedule) {
                                updateAirPageAdapterAndDescriptionContainer(tvSchedule.getScheduleForChannel());
                            }
                        });
            }
        });
    }

    private void triggerLoadAdditionalChannelItems(int lastCompletelyVisibleItemPosition) {
        if (mLastlyRequestedLoadingForChannelPosition != lastCompletelyVisibleItemPosition) {
            Log.e(TAG, "triggerLoadAdditionalChannelItems");
            mLastlyRequestedLoadingForChannelPosition = lastCompletelyVisibleItemPosition;

            loadChannelsDataForPage(mCurrentTenSelectedChannels + 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new AbstractRxSubscriber<TvChannels>() {
                        @Override
                        public void onNext(TvChannels loadedData) {
                            mCurrentTenSelectedChannels = loadedData.getPageNumber();
                            mChannelsListView.appendData(loadedData.getChannels());
                            mLastlyRequestedLoadingForChannelPosition = 0;
                        }
                    });
        }
    }

    private void bindBottomDescriptionContainer(int position) {
        mAiringTitleTextView.setText(mAirViewPager.getTitle(position));
        loadDescriptionOfSelectedAir(mAirViewPager.getSynopsisId(position));
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

    private void loadDescriptionOfSelectedAir(final String synopsisId) {
        Log.e(TAG, "loadDescriptionOfSelectedAir: " + synopsisId);
        mSynopsisDao.getSynopsis(getUrlForAirSynopsis(synopsisId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbstractRxSubscriber<AirSynopsis>() {
                    @Override
                    public void onNext(AirSynopsis airSynopsis) {
                        mAiringDescriptionTextView.setText(airSynopsis.getAirSynopsis().getSynopsis());
                    }

                    @Override
                    public void onError(Throwable e) {
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

    private void updateAirPageAdapterAndDescriptionContainer(Schedule airSchedule) {
        final int currentAirFromSchedule = getCurrentAirPositionFromSchedule(airSchedule);
        if (currentAirFromSchedule == NOT_DEFINED_AIRING_POSITION) {
            return;
        }

        final List<SimpleAiringObject> airingsData = extractAiringsFromSchedule(airSchedule, currentAirFromSchedule);
        mAirViewPager.update(airingsData);
        mAirViewPager.setCurrentItem(1);

        bindBottomDescriptionContainer(1);
    }

    private List<SimpleAiringObject> extractAiringsFromSchedule(Schedule airSchedule, int currentAirFromSchedule) {
        List<SimpleAiringObject> list = new ArrayList<>();
        for (int i = 0; i < PAGE_COUNT; i++) {
            boolean isSingleAir = airSchedule.getAirings().size() == 1;
            boolean isFirstAirOfTheDay = currentAirFromSchedule == 0 && i <= 1;
            boolean isLastAirOfTheDay = airSchedule.getAirings().size() == currentAirFromSchedule + 1
                    && i > 1;

            final Airing airing;
            if (isSingleAir || isFirstAirOfTheDay || isLastAirOfTheDay) {
                airing = airSchedule.getAirings().get(currentAirFromSchedule);
            } else {
                airing = airSchedule.getAirings().get(currentAirFromSchedule + i - 1);
            }

            list.add(SimpleAiringObject.from(airing));
        }
        return list;
    }

    private int getCurrentAirPositionFromSchedule(Schedule airSchedule) {
        final long currentTimeInMs = System.currentTimeMillis();
        for (int i = 0; i < airSchedule.getAirings().size(); i++) {
            if (airSchedule.getAirings().get(i).getStartAir().getTime() <= currentTimeInMs
                    && currentTimeInMs <= airSchedule.getAirings().get(i).getEndAir().getTime()) {
                return i;
            }
        }
        return NOT_DEFINED_AIRING_POSITION;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mCurrentlySelectedChannelPosition", mCurrentlySelectedChannelPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentlySelectedChannelPosition = savedInstanceState.getInt("mCurrentlySelectedChannelPosition");
        }
    }
}
