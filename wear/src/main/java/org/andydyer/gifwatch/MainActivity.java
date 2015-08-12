package org.andydyer.gifwatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.andydyer.gifwatch.rx.WearGifObservable;

import java.io.IOException;
import java.io.InputStream;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends WearableActivity {

    private BoxInsetLayout containerView;
    private TextView ambientText;
    private GifImageView gifImageView;
    private ProgressBar progress;
    private Observable<InputStream> gifObservable;
    private Subscription gifSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        containerView = (BoxInsetLayout) findViewById(R.id.container);
        ambientText = (TextView) findViewById(R.id.ambient_text);
        progress = (ProgressBar) findViewById(android.R.id.progress);
        gifImageView = (GifImageView) findViewById(R.id.gif_image_view);
        gifImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRandomGif();
            }
        });
        gifObservable = WearGifObservable.createObservable(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        maybeUnsubscribe();
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        progress.setVisibility(View.GONE);

        if (isAmbient()) {
            containerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            ambientText.setVisibility(View.VISIBLE);
            gifImageView.setVisibility(View.GONE);
            maybeUnsubscribe();
        } else {
            containerView.setBackground(null);
            ambientText.setVisibility(View.GONE);
            gifImageView.setVisibility(View.VISIBLE);
            subscribe();
        }
    }

    private void subscribe() {
        gifSubscription = gifObservable.subscribe(new Subscriber<InputStream>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onNext(InputStream inputStream) {
                progress.setVisibility(View.GONE);

                try {
                    byte[] bytes = Utils.inputStreamToByteArray(inputStream);
                    GifDrawable gifDrawable = new GifDrawable(bytes);
                    gifImageView.setImageDrawable(gifDrawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void maybeUnsubscribe() {
        if (gifSubscription != null && !gifSubscription.isUnsubscribed()) {
            gifSubscription.unsubscribe();
        }
    }

    private void requestRandomGif() {
        progress.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, GifRequestIntentService.class);
        startService(intent);
    }
}
