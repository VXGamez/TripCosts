package com.vx.dyvide.controller.restAPI.HERE.managers;


import android.content.Context;

import com.vx.dyvide.controller.restAPI.HERE.HEREInfo;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
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

        mainRetrofit = new Retrofit.Builder()
                .baseUrl("https://fuel-v2.cc.api.here.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    private void requestToken(){

    }

}

