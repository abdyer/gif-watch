package org.andydyer.gifwatch;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.andydyer.gifwatch.api.GiphyApiService;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;

/**
 * Created by andy on 8/11/15.
 */
public class GifWatchApplication extends Application {

    private static GifWatchApplication instance;

    private GiphyApiService giphyApiService;

    public GifWatchApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApiService();
    }

    private void initApiService() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.giphy.com/v1")
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("GiphyApiService"))
                .build();
        giphyApiService = restAdapter.create(GiphyApiService.class);
    }

    public static GifWatchApplication getInstance() {
        return instance;
    }

    public GiphyApiService getGiphyApiService() {
        return giphyApiService;
    }
}
