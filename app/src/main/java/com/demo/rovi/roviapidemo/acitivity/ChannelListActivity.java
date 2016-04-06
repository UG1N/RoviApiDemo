package com.demo.rovi.roviapidemo.acitivity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.damnhandy.uri.template.UriTemplate;
import com.demo.rovi.roviapidemo.R;
import com.demo.rovi.roviapidemo.adapter.AiringPageAdapter;
import com.demo.rovi.roviapidemo.adapter.ChannelListAdapter;
import com.demo.rovi.roviapidemo.application.RoviApplication;
import com.demo.rovi.roviapidemo.model.BackendConstants;
import com.demo.rovi.roviapidemo.model.Template.TemplateFile;
import com.demo.rovi.roviapidemo.model.TvChannels.Channel;
import com.demo.rovi.roviapidemo.model.TvChannels.TvChannels;
import com.demo.rovi.roviapidemo.model.TvSchedule.Schedule;
import com.demo.rovi.roviapidemo.model.TvSchedule.TvSchedule;
import com.demo.rovi.roviapidemo.model.dao.ChannelsDao;
import com.demo.rovi.roviapidemo.model.dao.ScheduleDao;
import com.demo.rovi.roviapidemo.model.dao.SynopsisDao;
import com.demo.rovi.roviapidemo.model.internal.SimpleAiringObject;
import com.demo.rovi.roviapidemo.model.restapi.IChannelsRestApi;
import com.demo.rovi.roviapidemo.model.restapi.IDataLoadingCallback;
import com.demo.rovi.roviapidemo.model.restapi.IScheduleRestApi;
import com.demo.rovi.roviapidemo.model.restapi.ISynopsesRestApi;
import com.demo.rovi.roviapidemo.model.synopses.AirSynopsis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChannelListActivity extends AppCompatActivity {

    private static final String TAG = ChannelListActivity.class.getSimpleName();
    public static final int PAGE_COUNT = 3;

    private TemplateFile mTemplateFile;
    private ChannelsDao mChannelsDao;
    private ScheduleDao mScheduleDao;
    private SynopsisDao mSynopsisDao;
    private TvChannels mTvChannels;
    private ChannelListAdapter mChannelListAdapter;

    private int lastlyRequestedLoadingForChannelPosition;
    private String[] mAirTitles = new String[PAGE_COUNT];
    private String[] mBeforeNowAfterAirId = new String[PAGE_COUNT];
    private String[] mAirsDescription = new String[PAGE_COUNT];
    private List<SimpleAiringObject> mSimpleAiringObjectList;

    private AiringPageAdapter mAiringPageAdapter;

    @Bind(R.id.recycler_view)
    RecyclerView mChannelsListView;

    @Bind(R.id.airing_page)
    ViewPager mViewPager;

    @Bind(R.id.airing_title)
    TextView mAiringTitleTextView;

    @Bind(R.id.airing_description)
    TextView mAiringDescriptionTextView;
    private SimpleAiringObject mSimpleAiringObject;
    private int currentPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_list_activity);
        if (savedInstanceState == null) {
            currentPage = 1;
        }
        // TODO: 28.03.2016 ButterKnife
        ButterKnife.bind(this);

        mTemplateFile = RoviApplication.getInstance().getTemplateFile();
        mSimpleAiringObjectList = new ArrayList<>();
        mSimpleAiringObject = new SimpleAiringObject();

        initDao();
        initChannelsList();
        mAiringPageAdapter = new AiringPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAiringPageAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("PageAdapter", "onCreate: ");
                if (mAirsDescription[position] != null) {
                    bindView(position);
                    Log.e(TAG, position + " " + mAirsDescription[position]);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void bindView(int position) {
        mAiringTitleTextView.setText(mAirTitles[position]);
        loadDescriptionOfSelectedAir(mAirsDescription[position]);
    }

    private void initDao() {
        String getUrl = UriTemplate
                .fromTemplate(mTemplateFile.getTemplate().getServiceChannels().getChannels())
                .set("id", BackendConstants.TELEVISION_SERVICE)
                .set("page", 1)
                .expand();
        Log.e(TAG, getUrl);
        mChannelsDao = new ChannelsDao(RoviApplication.createRestApiServiceImpl(IChannelsRestApi.class));
        mChannelsDao.getChannels(getUrl, new IDataLoadingCallback<TvChannels>() {
            @Override
            public void onResult(TvChannels loadedData) {
                for (Channel ch :
                        loadedData.getChannels()) {
                    Log.e("DAO ->", ch.toString());
                }
                mTvChannels = loadedData;
                mChannelListAdapter = new ChannelListAdapter(ChannelListActivity.this,
                        Arrays.asList(loadedData.getChannels()),
                        new ChannelListAdapter.ChannelLogoClickListener() {
                            @Override
                            public void onChannelClick(int position) {
                                currentPage = position;
                                Log.e(TAG, "onChannelClick: " + position);
                                loadScheduleDataForChannel(position);
                            }
                        });

                mChannelsListView.setAdapter(mChannelListAdapter);
                loadScheduleDataForChannel(currentPage);
            }

            @Override
            public void onFailure(Throwable ex) {
                Log.e(TAG, "Error occurred while loading channels data. ", ex);
            }
        });
    }

    private void loadScheduleDataForChannel(final int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String getUrl = UriTemplate
                .fromTemplate(mTemplateFile.getTemplate().getServiceSchedule().getSingleChannelSchedule())
                .set("id", BackendConstants.TELEVISION_SERVICE)
                .set("date", simpleDateFormat.format(new Date()))
                .set("page", position)
                .expand();
        mScheduleDao = new ScheduleDao(RoviApplication.createRestApiServiceImpl(IScheduleRestApi.class));
        mScheduleDao.getSchedule(getUrl, new IDataLoadingCallback<TvSchedule>() {
            @Override
            public void onResult(TvSchedule loadedData) {
                updateAirPageAdapter(loadedData.getScheduleForChannel());
            }

            @Override
            public void onFailure(Throwable ex) {
                Log.e(TAG, "Error occurred while loading channels data. ", ex);
            }
        });
    }

    private void updateAirPageAdapter(Schedule airSchedule) {
        int currentAirFromSchedule = 0;
        long currentTimeInMs = System.currentTimeMillis();
        for (int i = 0; i < airSchedule.getAirings().length; i++) {
            if (airSchedule.getAirings()[i].getStartAir().getTime() <= currentTimeInMs
                    && currentTimeInMs <= airSchedule.getAirings()[i].getEndAir().getTime()) {
                currentAirFromSchedule = i;
                break;
            }
        }

        for (int i = 0; i < PAGE_COUNT; i++) {
            if (airSchedule.getAirings().length == 1 && currentAirFromSchedule == 0) {
                getAirFromSchedule(i, airSchedule, currentAirFromSchedule);
            } else if (currentAirFromSchedule == 0 && i <= 1) {
                getAirFromSchedule(i, airSchedule, currentAirFromSchedule);
            } else if (currentAirFromSchedule == 0) {
                getAirFromSchedule(i, airSchedule, currentAirFromSchedule + 1);
            } else if (airSchedule.getAirings().length == currentAirFromSchedule + 1 && i > 1) {
                getAirFromSchedule(i, airSchedule, currentAirFromSchedule);
            } else {
                getAirFromSchedule(i, airSchedule, currentAirFromSchedule + i - 1);
            }
        }

        mAiringPageAdapter.updateAirPage(mBeforeNowAfterAirId);
        mViewPager.setCurrentItem(1);
        bindView(1);

        mAiringPageAdapter.updateAirPage2(mSimpleAiringObjectList);
    }

    private void getAirFromSchedule(int arrayPosition, Schedule schedule, int numberOfAir) {
        if (schedule.getAirings()[numberOfAir].getMediaImage() != null) {
            mBeforeNowAfterAirId[arrayPosition] = String.valueOf(schedule.getAirings()[numberOfAir]
                    .getMediaImage().getMediaImageReferences().getId());

            mSimpleAiringObject.setImageIconId(String.valueOf(schedule.getAirings()[numberOfAir]
                    .getMediaImage().getMediaImageReferences().getId()));

        } else {
            mBeforeNowAfterAirId[arrayPosition] = String.valueOf(-1);

            mSimpleAiringObject.setImageIconId(String.valueOf(-1));
        }
        mAirTitles[arrayPosition] = schedule.getAirings()[numberOfAir].getAiringTitle();
        mAirsDescription[arrayPosition] = String.valueOf(schedule.getAirings()[numberOfAir]
                .getAiringReferences().getId());

        mSimpleAiringObject.setSynopsisId(String.valueOf(schedule.getAirings()[numberOfAir]
                .getAiringReferences().getId()));

        mSimpleAiringObject.setTitle(schedule.getAirings()[numberOfAir].getAiringTitle());

        mSimpleAiringObjectList.add(arrayPosition, mSimpleAiringObject);
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
        String urlForSynopsis = UriTemplate
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
        return urlForSynopsis;
    }

    private void initChannelsList() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mChannelsListView.setLayoutManager(linearLayoutManager);
        mChannelsListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                final int lastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                boolean isLastViewDisplayed = lastCompletelyVisibleItemPosition
                        == recyclerView.getAdapter().getItemCount() - 1;
                boolean isLastPage = mTvChannels.getTotalChannels() == lastCompletelyVisibleItemPosition + 1;

                Log.e(TAG, String.valueOf(lastCompletelyVisibleItemPosition + 1) + " " + mTvChannels.getTotalChannels());

                if (newState == RecyclerView.SCROLL_STATE_IDLE && isLastViewDisplayed && !isLastPage) {
                    triggerLoadAdditionalChannelItems(lastCompletelyVisibleItemPosition);
                }
            }
        });
    }

    private void triggerLoadAdditionalChannelItems(int lastCompletelyVisibleItemPosition) {
        if (lastlyRequestedLoadingForChannelPosition != lastCompletelyVisibleItemPosition) {
            Log.e(TAG, "triggerLoadAdditionalChannelItems");
            // TODO: 31.03.2016 do loading
            lastlyRequestedLoadingForChannelPosition = lastCompletelyVisibleItemPosition;

            String getUrl = UriTemplate
                    .fromTemplate(mTemplateFile.getTemplate().getServiceChannels().getChannels())
                    .set("id", BackendConstants.TELEVISION_SERVICE)
                    // next page --> current + 1;
                    .set("page", mTvChannels.getPageNumber() + 1)
                    .expand();

            mChannelsDao.getChannels(getUrl, new IDataLoadingCallback<TvChannels>() {
                @Override
                public void onResult(TvChannels loadedData) {
                    for (Channel ch :
                            loadedData.getChannels()) {
                        Log.e("DAO ADD->", ch.toString());
                    }
                    mTvChannels = loadedData;
                    mChannelListAdapter.addNewChannels(Arrays.asList(loadedData.getChannels()));
                    lastlyRequestedLoadingForChannelPosition = 0;
                }

                @Override
                public void onFailure(Throwable ex) {
                    Log.e(TAG, "Error occurred while loading channels data. ", ex);
                }
            });
        }
    }

    // TODO: 05.04.2016
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPage", currentPage);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt("currentPage");
        }
    }
}
