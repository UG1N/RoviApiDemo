package com.demo.rovi.roviapidemo.acitivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.demo.rovi.roviapidemo.R;
import com.demo.rovi.roviapidemo.adapter.ChannelListAdapter;
import com.demo.rovi.roviapidemo.model.ApiEndpointInterface;
import com.demo.rovi.roviapidemo.model.IDataLoadingCallback;
import com.demo.rovi.roviapidemo.model.TvChannels.Channel;
import com.demo.rovi.roviapidemo.model.TvSchedule.TvSchedule;
import com.demo.rovi.roviapidemo.utils.OAuthSignatureGenerator;
import com.demo.rovi.roviapidemo.utils.TvApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChannelListActivity extends AppCompatActivity {

    private static final String TAG = ChannelListActivity.class.getCanonicalName();

    private String mOauthHeader;
    private String mUrl;

    private ImageView mImageView;

    private RecyclerView mChannelsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDaos();
        setContentView(R.layout.channel_list_activity);

        // TODO: 28.03.2016 ButterKnife
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mChannelsListView = (RecyclerView) findViewById(R.id.recycler_view);
        mChannelsListView.setLayoutManager(linearLayoutManager);

        loadAndDisplayChannelsData();

        mImageView = (ImageView) findViewById(R.id.item_image);

//        getChannelSchedule();

    }

    private void initDaos() {
//        new ChannelsDao(App.retrofit().create(I))
    }

    private void loadAndDisplayChannelsData() {
        TvApi.channelBuilder().build(new IDataLoadingCallback<List<Channel>>() {
            @Override
            public void onResult(List<Channel> loadedData) {
                ChannelListAdapter channelListAdapter = new ChannelListAdapter(ChannelListActivity.this, loadedData);
                mChannelsListView.setAdapter(channelListAdapter);
            }

            @Override
            public void onFailure(Throwable ex) {
                Log.e(TAG, "Error occurred while loading channels data. ", ex);
            }
        });
    }

    private void getChannelSchedule() {
        try {
            String httpMethod = "GET";
            mUrl = "http://cloud.rovicorp.com/data/2/2.4/lookup/service/";
            String consumerKey = "86273bfd5ea822f5c455ddde5c527a3f884523955edeece10b7ae32efcd3963e";
            String consumerSecret = "bc2a235f23470733fadcd54bc38d6c01216322d27506022e3e4d7852a8c20d28";

            OAuthSignatureGenerator oauth = new OAuthSignatureGenerator();
            // overriding nonce and timestamp

            String signature =
                    oauth.generateSignature(httpMethod, mUrl + "1952967572;offering=AU,BR,CA,CN,IN,EUR,LTA,RU,SEA,US,TR/schedule/2016-03-25?page=1&size=1&duration=24", consumerKey, consumerSecret);
            mOauthHeader = oauth.buildAuthorizationHeader(consumerKey, signature);

            System.out.println("Authorization Header:\n" + mOauthHeader);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", mOauthHeader).build();
                return chain.proceed(newRequest);
            }
        };
        OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        ApiEndpointInterface apiService = retrofit.create(ApiEndpointInterface.class);
        Call<TvSchedule> call = apiService.getScheduleDataFromUrl("1952967572;offering=AU,BR,CA,CN,IN,EUR,LTA,RU,SEA,US,TR/schedule/2016-03-25?page=1&size=1&duration=24");
        call.enqueue(new Callback<TvSchedule>() {
            @Override
            public void onResponse(Call<TvSchedule> call, retrofit2.Response<TvSchedule> response) {
                TvSchedule tvSchedule = response.body();

                Log.e("Activity", call.toString() + "\n" + response.message() + "\n" + response.code());
                Log.e("Activity", call.request().url().toString());
                String imageId = null;
                for (int i = 0; i < tvSchedule.getScheduleForSingleChannel()[0].getAirings().length; i++) {
                    Log.e("Activity", tvSchedule.getScheduleForSingleChannel()[0].getAirings()[i].toString());

                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH : mm, EEEE", Locale.ENGLISH);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                for (int i = 0; i < tvSchedule.getScheduleForSingleChannel()[0].getAirings().length; i++) {
                    Log.e("Activity", "Start: " + simpleDateFormat.format(tvSchedule.getScheduleForSingleChannel()[0].getAirings()[i].getStartAir()));
                    Log.e("Activity", "End:   " + simpleDateFormat.format(tvSchedule.getScheduleForSingleChannel()[0].getAirings()[i].getEndAir().getTime()));
                    if (tvSchedule
                            .getScheduleForSingleChannel()[0]
                            .getAirings()[i]
                            .getMediaImage() != null) {
                        imageId = String.valueOf(tvSchedule
                                .getScheduleForSingleChannel()[0]
                                .getAirings()[i]
                                .getMediaImage().getMediaImageReferences().getId());
                        break;
                    }
                }
                String logoUrl = "http://cloud.rovicorp.com/media/v1/source/" + imageId;

                Log.e("Activity", logoUrl);
//                Glide.with(ChannelListActivity.this)
//                        .load(logoUrl)
//                        .into(mImageView);
            }

            @Override
            public void onFailure(Call<TvSchedule> call, Throwable t) {
                Log.e("Error", call.request().url().toString() + "\n" + t.getMessage() + "\n" +
                        t.getStackTrace()[0]);
                t.printStackTrace();
            }
        });
    }

}
