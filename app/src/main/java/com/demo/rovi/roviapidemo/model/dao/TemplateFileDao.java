package com.demo.rovi.roviapidemo.model.dao;

import com.demo.rovi.roviapidemo.model.Template.TemplateFile;
import com.demo.rovi.roviapidemo.model.restapi.IDataLoadingCallback;
import com.demo.rovi.roviapidemo.model.restapi.ITemplateRestApi;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TemplateFileDao {
    private final ITemplateRestApi mITemplateRestApi;

    public TemplateFileDao(ITemplateRestApi ITemplateRestApi) {
        this.mITemplateRestApi = ITemplateRestApi;
    }

    public void getTemplateFile(String urlToLoadTemplateFile, final IDataLoadingCallback<TemplateFile> loadingCallback) {
        mITemplateRestApi.getTemplateFile(urlToLoadTemplateFile).enqueue(
                new Callback<TemplateFile>() {
                    @Override
                    public void onResponse(Response<TemplateFile> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            loadingCallback.onResult(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // TODO: 28.03.2016
                        loadingCallback.onFailure(t);
                    }
                }
        );
    }
}
