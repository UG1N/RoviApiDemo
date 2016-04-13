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
import com.demo.rovi.roviapidemo.model.restapi.IChannelsRestApi;
import com.demo.rovi.roviapidemo.model.restapi.IScheduleRestApi;
import com.demo.rovi.roviapidemo.model.restapi.ISynopsesRestApi;
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

    private int lastlyRequestedLoadingForChannelPosition;
    private int currentlySelectedChannelPosition;


    @Bind(R.id.recycler_view)
    ChannelsHorizontalListView mChannelsListView;

    @Bind(R.id.airing_page)
    AirViewPager mAirViewPager;

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

        initAiringsPager();
        loadInitialChannelsChunk();
        initChannelsListView();
    }

    private void initDaos() {
        mChannelsDao = new ChannelsDao(RoviApplication.createRestApiServiceImpl(IChannelsRestApi.class));
        mScheduleDao = new ScheduleDao(RoviApplication.createRestApiServiceImpl(IScheduleRestApi.class));
        mSynopsisDao = new SynopsisDao(RoviApplication.createRestApiServiceImpl(ISynopsesRestApi.class));
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
                loadScheduleDataForChannel(channelClickedPosition)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new AbstractRxSubscriber<TvSchedule>() {
                            @Override
                            public void onNext(TvSchedule tvSchedule) {
                                updateAirPageAdapter(tvSchedule.getScheduleForChannel());
                            }
                        });
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

    private void bindBottomDescriptionContainer(int position) {
        mAiringTitleTextView.setText(mAirViewPager.getTitle(position));
        loadDescriptionOfSelectedAir(mAirViewPager.getSynopsisId(position));
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

    private Observable<TvChannels> loadChannelsDataForPage(int pageNumber) {
        String channelsLoadingUrl = UriTemplate
                .fromTemplate(mTemplateFile.getTemplate().getServiceChannels().getChannels())
                .set("id", BackendConstants.TELEVISION_SERVICE)
                .set("page", pageNumber)
                .expand();
        return mChannelsDao.getChannels(channelsLoadingUrl);
    }

    private void loadDescriptionOfSelectedAir(final String urlId) {
        Log.e(TAG, "url id in if- > " + urlId);

        mSynopsisDao.getSynopsis(getUrlForAirSynopsis(urlId))
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

    private void updateAirPageAdapter(Schedule airSchedule) {
        List<SimpleAiringObject> list = new ArrayList<>();
        int currentAirFromSchedule = 0;
        long currentTimeInMs = System.currentTimeMillis();

        for (int i = 0; i < airSchedule.getAirings().size(); i++) {
            if (airSchedule.getAirings().get(i).getStartAir().getTime() <= currentTimeInMs
                    && currentTimeInMs <= airSchedule.getAirings().get(i).getEndAir().getTime()) {
                currentAirFromSchedule = i;
                break;
            }
        }
        // - deprecated,  smth with this method
        for (int i = 0; i < PAGE_COUNT; i++) {
            if (airSchedule.getAirings().size() == 1 && currentAirFromSchedule == 0) {
                list.add(getAirFromSchedule(airSchedule, currentAirFromSchedule));
            } else if (currentAirFromSchedule == 0 && i <= 1) {
                list.add(getAirFromSchedule(airSchedule, currentAirFromSchedule));
            } else if (currentAirFromSchedule == 0) {
                list.add(getAirFromSchedule(airSchedule, currentAirFromSchedule + 1));
            } else if (airSchedule.getAirings().size() == currentAirFromSchedule + 1 && i > 1) {
                list.add(getAirFromSchedule(airSchedule, currentAirFromSchedule));
            } else {
                list.add(getAirFromSchedule(airSchedule, currentAirFromSchedule + i - 1));
            }
        }

        mAirViewPager.update(list);
        mAirViewPager.setCurrentItem(1);

        bindBottomDescriptionContainer(1);
    }

    private SimpleAiringObject getAirFromSchedule(Schedule schedule, int numberOfAir) {
        String imageId;
        if (schedule.getAirings().get(numberOfAir).getMediaImage() != null) {
            imageId = (String.valueOf(schedule.getAirings().get(numberOfAir)
                    .getMediaImage().getMediaImageReferences().getId()));
        } else {
            imageId = (String.valueOf(-1));
        }
        String synopsisId = String.valueOf(schedule.getAirings().get(numberOfAir)
                .getAiringReferences().getId());
        String title = schedule.getAirings().get(numberOfAir).getAiringTitle();

        return new SimpleAiringObject(synopsisId, imageId, title);
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
