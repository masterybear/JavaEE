package org.example.factoryDI;

import okhttp3.OkHttpClient;

public class OkHttpFactory {
    private OkHttpClient okHttpClient;

    public OkHttpClient getInstance() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder().build();
        }
        return okHttpClient;
    }
}
