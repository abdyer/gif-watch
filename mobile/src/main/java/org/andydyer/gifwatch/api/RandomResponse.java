package org.andydyer.gifwatch.api;

/**
 * Response body for a Giphy random sticker request
 */
public class RandomResponse {

    private Data data;
    private Meta meta;

    public Data getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }
}
