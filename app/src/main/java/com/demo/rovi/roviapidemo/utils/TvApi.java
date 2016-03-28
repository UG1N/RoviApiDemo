package com.demo.rovi.roviapidemo.utils;

import android.util.Log;

import com.demo.rovi.roviapidemo.model.ApiEndpointInterface;
import com.demo.rovi.roviapidemo.model.IDataLoadingCallback;
import com.demo.rovi.roviapidemo.model.TvChannels.Channel;
import com.demo.rovi.roviapidemo.model.TvChannels.TvChannels;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvApi {

    public static ChannelBuilder channelBuilder() {
        return new ChannelBuilder();
    }

    public static class ChannelBuilder {
        private int mPageOfChannelList;
        private String mUrl;
        private String mOauthHeader;

        public ChannelBuilder pageNumber(int page) {
            mPageOfChannelList = page;
            return this;
        }

        public void build(final IDataLoadingCallback<List<Channel>> dataLoadingCallback) {
            try {
                String httpMethod = "GET";
                mUrl = "http://cloud.rovicorp.com/data/2/2.4/lookup/service/";
                String consumerKey = "86273bfd5ea822f5c455ddde5c527a3f884523955edeece10b7ae32efcd3963e";
                String consumerSecret = "bc2a235f23470733fadcd54bc38d6c01216322d27506022e3e4d7852a8c20d28";

                OAuthSignatureGenerator oauth = new OAuthSignatureGenerator();
                // overriding nonce and timestamp

                String signature =
                        oauth.generateSignature(httpMethod, mUrl + "1952967572;offering=AU,BR,CA,CN,IN,EUR,LTA,RU,SEA,US,TR/channels?idns=iguide,program,service,source&page=1&size=10", consumerKey, consumerSecret);
                mOauthHeader = oauth.buildAuthorizationHeader(consumerKey, signature);

                System.out.println("Authorization Header:\n" + mOauthHeader);
                // TODO: 28.03.2016 Not good
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
            Call<TvChannels> call = apiService.getChannelsDataFromUrl("1952967572;offering=AU,BR,CA,CN,IN,EUR,LTA,RU,SEA,US,TR/channels?idns=iguide,program,service,source&page=1&size=10");
            call.enqueue(new Callback<TvChannels>() {
                @Override
                public void onResponse(Call<TvChannels> call, retrofit2.Response<TvChannels> response) {
                    TvChannels tvChannels = response.body();
                    final List<Channel> loadedChannels = new ArrayList<>();
                    loadedChannels.addAll(Arrays.asList(tvChannels.getChannels()));
                    Log.e("Activity", call.toString() + "\n" + response.message() + "\n" + response.code());
                    Log.e("Activity", call.request().url().toString());
                    for (int i = 0; i < tvChannels.getChannels().length; i++) {
                        Log.e("Activity", tvChannels.getChannel(i).toString());
                    }
                    String logoUrl = "http://cloud.rovicorp.com/media/v1/logo/large/"
                            + tvChannels
                            .getChannel(0)
                            .getWindowChannels()[0]
                            .getDataSources()
                            .getLogo()
                            .getLogoReferences()
                            .getId()
                            + ".png";
                    Log.e("Activity", logoUrl);
//                    Glide.with(ChannelListActivity.this)
//                            .load(logoUrl)
//                            .into(mImageView);

                    dataLoadingCallback.onResult(loadedChannels);
                }

                @Override
                public void onFailure(Call<TvChannels> call, Throwable t) {
                    dataLoadingCallback.onFailure(t);
                    Log.e("Error", call.request().url().toString() + "\n" + t.getMessage() + "\n" +
                            t.getStackTrace()[0]);
                    t.printStackTrace();
                }
            });
        }
    }
}
