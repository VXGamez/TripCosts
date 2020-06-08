package com.vx.dyvide.controller.restAPI.Michelin.managers;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.vx.dyvide.controller.restAPI.Michelin.MichelinINFO;
import com.vx.dyvide.controller.restAPI.Michelin.callbacks.MichelinCallback;
import com.vx.dyvide.controller.restAPI.Michelin.service.MichelinService;
import com.vx.dyvide.model.Michelin.Iti;
import com.vx.dyvide.model.Michelin.Resposta;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RouteManager {
    private Retrofit mainRetrofit;
    private static final String TAG = RouteManager.class.getName();
    private MichelinService michelinService;
    private Context mContext;
    private static RouteManager sPlaylistManager;


    public static RouteManager getInstance(Context context) {
        if (sPlaylistManager == null) {
            sPlaylistManager = new RouteManager(context);
        }
        return sPlaylistManager;
    }


    public RouteManager(Context context) {
        mainRetrofit = new Retrofit.Builder()
                .baseUrl("https://secure-apir.viamichelin.com/apir/")
                //.addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        mContext = context;
        michelinService = mainRetrofit.create(MichelinService.class);
    }

    private String makeSteps(LatLng origin, LatLng destination){
        String s =  "1:p:"+origin.longitude+":"+origin.latitude+";1:p:"+destination.longitude+":"+destination.latitude;
        return s;
    }

    private String makeConsumption(float consumption){
        String c = consumption+";"+consumption+";"+consumption;
        return c;
    }

    public synchronized void getRouteHeader(LatLng origin, LatLng destination, int vehicleType, float consumption, float fuelCost, final MichelinCallback callback) {

        //vehicle type: 0:Car | 1:Truck | 2:On foot | 3: Cycle | 4:Moto

        Call<String> call = michelinService.getHeader(makeSteps(origin, destination), vehicleType, makeConsumption(consumption), fuelCost, "EUR",  MichelinINFO.API_KEY, "onResponse");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    callback.onHeaderRecieved(parseJson(response.body()));
                } else {
                    try {
                        callback.onHeaderFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                callback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }

    private Resposta parseJson(String json) {
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder(json);

        sb.delete(0, 11);
        sb.deleteCharAt(sb.toString().length()-1);

        String result = sb.toString();

        return gson.fromJson(result, Resposta.class);
    }
}
