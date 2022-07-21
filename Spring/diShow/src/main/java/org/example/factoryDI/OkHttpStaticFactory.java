package org.example.factoryDI;

import okhttp3.OkHttpClient;

public class OkHttpStaticFactory {
    private static OkHttpClient okHttpClient;

    public static OkHttpClient getInstance() {
        if (okHttpClient == null){
            okHttpClient = new OkHttpClient.Builder().build();
        }
        return okHttpClient;
    }
}
