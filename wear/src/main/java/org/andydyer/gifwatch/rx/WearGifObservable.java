package org.andydyer.gifwatch.rx;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;

/**
 * Observes GIFs pushed by mobile app
 */
public class WearGifObservable extends BaseObservable<InputStream> {

    private DataApi.DataListener dataListener;

    public static Observable<InputStream> createObservable(Context context) {
        return Observable.create(new WearGifObservable(context));
    }

    private WearGifObservable(Context context) {
        super(context, Wearable.API);
    }

    @Override
    protected void onGoogleApiClientReady(final GoogleApiClient googleApiClient,
                                          final Observer<? super InputStream> observer) {
        dataListener = new DataApi.DataListener() {
            @Override
            public void onDataChanged(DataEventBuffer dataEvents) {
                // Emit each image as an InputStream
                for (DataEvent event : dataEvents) {
                    if (event.getType() == DataEvent.TYPE_CHANGED) {
                        DataItem item = event.getDataItem();
                        if (item.getUri().getPath().compareTo("/image") == 0) {
                            Asset asset =
                                    DataMapItem.fromDataItem(item).getDataMap().get("random_gif");
                            InputStream inputStream = assetToInputStream(googleApiClient, asset);
                            observer.onNext(inputStream);
                        }
                    }
                }
            }
        };
        Wearable.DataApi.addListener(googleApiClient, dataListener);
    }

    @Override
    protected void onUnsubscribed(GoogleApiClient googleApiClient) {
        if (googleApiClient.isConnected()) {
            Wearable.DataApi.removeListener(googleApiClient, dataListener);
        }
    }

    private InputStream assetToInputStream(GoogleApiClient googleApiClient, Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        ConnectionResult result = googleApiClient.blockingConnect(30, TimeUnit.MILLISECONDS);
        if (!result.isSuccess()) {
            return null;
        }

        return Wearable.DataApi.getFdForAsset(googleApiClient, asset).await().getInputStream();
    }
}
