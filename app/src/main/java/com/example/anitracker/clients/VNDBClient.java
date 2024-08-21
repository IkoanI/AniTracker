package com.example.anitracker.clients;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public enum VNDBClient {
    INSTANCE;
    final Retrofit client;
    VNDBClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        client = new Retrofit.Builder()
                .baseUrl("https://api.vndb.org/kana/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public Retrofit getClient(){
        return client;
    }

}
