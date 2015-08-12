package org.andydyer.gifwatch;

import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.andydyer.gifwatch.api.GiphyApiService;
import org.andydyer.gifwatch.api.RandomResponse;

import java.util.concurrent.TimeUnit;

import rx.schedulers.Schedulers;

/**
 * Sends a random GIF to wearable when triggered
 */
public class GifRequestWearableListenerService extends WearableListenerService {

    private final GiphyApiService apiService;

    public GifRequestWearableListenerService() {
        this.apiService = GifWatchApplication.getInstance().getGiphyApiService();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals("gif/random")) {
            apiService.getRandomSticker()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .filter(response -> response.getMeta().getStatus() == 200)
                    .map(this::downloadImage)
                    .filter(bytes -> bytes != null)
                    .map(imageBytes -> Asset.createFromBytes(imageBytes))
                    .subscribe(this::pushAsset);
        }
    }

    private byte[] downloadImage(RandomResponse random) {
        try {
            String url = random.getData().getFixedWidthDownsampledUrl();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            return response.body().bytes();
        }
        catch (Exception e) {
            return  null;
        }
    }

    private void pushAsset(Asset asset) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        ConnectionResult connectionResult = googleApiClient.blockingConnect(30, TimeUnit.SECONDS);
        if (!connectionResult.isSuccess()) {
            Log.d(getClass().getSimpleName(), "Error connecting Google API client");
        }

        PutDataMapRequest request = PutDataMapRequest.create("/image");
        request.getDataMap().putAsset("random_gif", asset);
        Wearable.DataApi.putDataItem(googleApiClient, request.asPutDataRequest());
    }
}
