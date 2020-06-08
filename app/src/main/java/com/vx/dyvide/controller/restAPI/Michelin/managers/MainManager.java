package com.vx.dyvide.controller.restAPI.Michelin.managers;


import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainManager {
    protected Retrofit mainRetrofit;

    public static final String TAG = "MainManager";
    protected Context mContext;
    private OkHttpClient client, mCachedOkHttpClient;


    public MainManager(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(500, TimeUnit.SECONDS).readTimeout(500, TimeUnit.SECONDS);


        client = httpClient.build();

        /*
        *https://secure-apir.viamichelin.com/apir/1/route.json/eng/header?steps=1:p:lon:lat;1:p:lon:lat&veht=0&itit=0&fuelConsump=7.7&fuelCost=1.4&cy=EUR&authkey=RESTGP20200608184108008590980051&callback=infoRecieved

https://secure-apir.viamichelin.com/apir/1/route.json/eng/roadsheet?steps=1:p:2.139490:41.409720;1:p:3.278390:42.288960&veht=0&itit=0&fuelConsump.city=7.7:7.7:7.7&fuelCost=1.48&cy=EUR&authkey=RESTGP20200608184108008590980051&callback=infoRecieved
        *
        * */

        mainRetrofit = new Retrofit.Builder()
                .baseUrl("https://secure-apir.viamichelin.com/apir/1/route.json/eng/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }


}

