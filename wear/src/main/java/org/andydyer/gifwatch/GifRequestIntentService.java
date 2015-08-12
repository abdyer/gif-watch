package org.andydyer.gifwatch;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Triggers mobile app to fetch and push a new GIF
 */
public class GifRequestIntentService extends IntentService {

    public GifRequestIntentService() {
        super("GifRequestIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        ConnectionResult connectionResult = googleApiClient.blockingConnect(30, TimeUnit.SECONDS);
        if (!connectionResult.isSuccess()) {
            Log.d(getClass().getSimpleName(), "Error connecting Google API client");
        }

        Set<Node> nodes =  Wearable.CapabilityApi.getCapability(googleApiClient, "gif_me",
                CapabilityApi.FILTER_REACHABLE).await()
                .getCapability().getNodes();
        if (!nodes.isEmpty()) {
            for (Node node : nodes) {
                Wearable.MessageApi.sendMessage(googleApiClient, node.getId(),
                        "gif/random", null).await();
            }
        }
    }
}
