package com.demo.rovi.roviapidemo.model.dao.auth;

import android.content.Context;

import com.demo.rovi.roviapidemo.model.BackendConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


public final class RestApiClientConfiguration {

    private static final int HTTP_CACHE_SIZE = 10 * 1024 * 1024;
    private static final String REST_API_CACHE_DIR_NAME = "rest_api_cache";

    private final Context mContext;
    private final CookieManager mDefaultCookieHandler;
    private final OkHttpClient mOkHttpClient;
    private final Retrofit mRetrofit;


    public RestApiClientConfiguration(Context context) {
        mContext = context;
        mDefaultCookieHandler = new CookieManager();
        this.mOkHttpClient = initOkHttpClient(mDefaultCookieHandler);
        mRetrofit = initRetrofit();
    }

    public <T> T createRestApiServiceImpl(Class<T> serviceApiInterface) {
        return mRetrofit.create(serviceApiInterface);
    }

    private OkHttpClient initOkHttpClient(CookieHandler defaultCookieHandler) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(BackendConstants.CONNECTING_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(BackendConstants.READ_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.setCookieHandler(defaultCookieHandler);
        Cache cache = createResponseCache();
        if (cache != null) {
            okHttpClient.setCache(createResponseCache());
        }

        //TODO: For What?
        addRequestInterceptors(okHttpClient);
        return okHttpClient;
    }

    private void addRequestInterceptors(OkHttpClient okHttpClient) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient.interceptors().add(interceptor);

        okHttpClient.interceptors().add(new OAuthRequestInterceptor());
    }

    private Cache createResponseCache() {
        File cacheDir = mContext.getDir(REST_API_CACHE_DIR_NAME, Context.MODE_PRIVATE);
        return new Cache(cacheDir, HTTP_CACHE_SIZE);
    }

    private Retrofit initRetrofit() {
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        return new Retrofit.Builder()
                .baseUrl(BackendConstants.ROVI_BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private static class OAuthRequestInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();

            Request.Builder fullRequestBuilder = originalRequest.newBuilder()
                    .header("Authorization", getAuthorizationHeader(originalRequest));
            return chain.proceed(fullRequestBuilder.build());
        }

        private String getAuthorizationHeader(Request originalRequest) {
            String oAuthHeader = "";
            OAuthSignatureGenerator oAuth = new OAuthSignatureGenerator();
            try {
                String signature = oAuth.generateSignature("GET", originalRequest.urlString(),
                        BackendConstants.CONSUMER_KEY, BackendConstants.CONSUMER_SECRET);
                oAuthHeader = oAuth.buildAuthorizationHeader(BackendConstants.CONSUMER_KEY, signature);

                return oAuthHeader;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            return oAuthHeader;
        }
    }
}
