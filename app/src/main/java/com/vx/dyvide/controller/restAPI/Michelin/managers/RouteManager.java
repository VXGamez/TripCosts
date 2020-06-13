package com.vx.dyvide.controller.restAPI.Michelin.managers;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vx.dyvide.controller.restAPI.Michelin.MichelinINFO;
import com.vx.dyvide.controller.restAPI.Michelin.callbacks.MichelinCallback;
import com.vx.dyvide.controller.restAPI.Michelin.service.MichelinService;
import com.vx.dyvide.model.Michelin.Header;
import com.vx.dyvide.model.Michelin.Iti;
import com.vx.dyvide.model.Michelin.ItiRoadsheet;
import com.vx.dyvide.model.Michelin.RoadSheet;
import com.vx.dyvide.model.Michelin.Summary;
import com.vx.dyvide.model.Vehicle;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

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
    private MichelinCallback mContext;
    private static RouteManager sPlaylistManager;


    public static RouteManager getInstance(MichelinCallback context) {
        if (sPlaylistManager == null) {
            sPlaylistManager = new RouteManager(context);
        }
        return sPlaylistManager;
    }


    public RouteManager(MichelinCallback context) {
        mainRetrofit = new Retrofit.Builder()
                .baseUrl("https://secure-apir.viamichelin.com/apir/")
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

    public synchronized void getRouteHeader(LatLng origin, LatLng destination, float consumption, float fuelCost, final MichelinCallback callback) {

        //vehicle type: 0:Car | 1:Truck | 2:On foot | 3: Cycle | 4:Moto

        Call<String> call = michelinService.getHeader(makeSteps(origin, destination), makeConsumption(consumption), fuelCost, "EUR",  MichelinINFO.API_KEY, "onResponse");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    callback.onHeaderRecieved(parseJsonIti(response.body()));
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

    public synchronized void getRouteRoadsheet(LatLng origin, LatLng destination, float consumption, float fuelCost, final MichelinCallback callback) {

        //vehicle type: 0:Car | 1:Truck | 2:On foot | 3: Cycle | 4:Moto

        Call<String> call = michelinService.getRoadsheet(makeSteps(origin, destination),makeConsumption(consumption), fuelCost, "EUR",  MichelinINFO.API_KEY, "onResponse");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    callback.onRoadSheetRecieved(parseJsonRoadsheet(response.body()));
                } else {
                    try {
                        callback.onRoadSheetFailure(new Throwable(response.errorBody().string()));
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

    private ArrayList<Object> parseJsonRoadsheet(String body) {
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder(body);

        sb.delete(0, 18);
        sb.deleteCharAt(sb.toString().length()-1);
        sb.deleteCharAt(sb.toString().length()-1);

        String result = sb.toString();
        if(result.contains("errorCode")){
            ArrayList<Object> o = new ArrayList<>();
            o.add("ERROR");
            return o;
        }else{
            ItiRoadsheet i = gson.fromJson(result, ItiRoadsheet.class);
            return (ArrayList<Object>) i.getRoadSheet().get(0);
        }


    }

    private Summary parseJsonIti(String json) {
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder(json);

        sb.delete(0, 18);
        sb.deleteCharAt(sb.toString().length()-1);
        sb.deleteCharAt(sb.toString().length()-1);

        String result = sb.toString();
        if(result.contains("errorCode")){
            return new Summary(null, -.1);
        }else{
            Iti h = gson.fromJson(result, Iti.class);
            ArrayList<Summary> r = (ArrayList<Summary>) h.getHeader().getSummaries();
            return r.get(0);
        }

    }
}
