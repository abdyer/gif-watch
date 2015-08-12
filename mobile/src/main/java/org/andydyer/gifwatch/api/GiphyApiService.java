package org.andydyer.gifwatch.api;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by andy on 8/11/15.
 */
public interface GiphyApiService {

    //TODO: Replace public demo API key
    @GET("/stickers/random?api_key=dc6zaTOxFJmzC")
    Observable<RandomResponse> getRandomSticker();
}
