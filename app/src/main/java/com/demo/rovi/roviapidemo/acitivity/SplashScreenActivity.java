package com.demo.rovi.roviapidemo.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.demo.rovi.roviapidemo.R;
import com.demo.rovi.roviapidemo.application.RoviApplication;
import com.demo.rovi.roviapidemo.model.BackendConstants;
import com.demo.rovi.roviapidemo.model.Template.TemplateFile;
import com.demo.rovi.roviapidemo.model.dao.TemplateFileDao;
import com.demo.rovi.roviapidemo.model.restapi.IDataLoadingCallback;
import com.demo.rovi.roviapidemo.model.restapi.ITemplateRestApi;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TemplateFileDao templateFileDao = new TemplateFileDao(RoviApplication.createRestApiServiceImpl(ITemplateRestApi.class));
        templateFileDao.getTemplateFile("http://cloud.rovicorp.com/template/v1/" +
                        "86273bfd5ea822f5c455ddde5c527a3f884523955edeece10b7ae32efcd3963e/2/templates.json",
                new IDataLoadingCallback<TemplateFile>() {
                    @Override
                    public void onResult(TemplateFile loadedData) {
                        Log.e("SplashScreenActivity", loadedData.toString());
                        Intent mainIntent = new Intent(SplashScreenActivity.this, ChannelListActivity.class);
                        mainIntent.putExtra(BackendConstants.TEMPLATE_FILE, loadedData);
                        SplashScreenActivity.this.startActivity(mainIntent);
                        SplashScreenActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Throwable ex) {
                        Toast.makeText(SplashScreenActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        SplashScreenActivity.this.finish();
                    }
                });
    }

}
