package com.vx.dyvide.controller.restAPI.HERE.managers;

import android.content.Context;
import android.util.Log;

import com.vx.dyvide.controller.restAPI.HERE.HEREInfo;
import com.vx.dyvide.controller.restAPI.HERE.callbacks.HereCallback;
import com.vx.dyvide.controller.restAPI.HERE.service.FuelService;
import com.vx.dyvide.model.PriceResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FuelManager extends MainManager {

    private static final String TAG = FuelManager.class.getName();
    private FuelService mPlaylistService;
    private static FuelManager sPlaylistManager;


    public static FuelManager getInstance(Context context) {
        if (sPlaylistManager == null) {
            sPlaylistManager = new FuelManager(context);
        }
        return sPlaylistManager;
    }

    public FuelManager(Context context) {
        mContext = context;
        mPlaylistService = mainRetrofit.create(FuelService.class);
    }

    public synchronized void getFuelPrice(int type, double lon, double lat, final HereCallback hereCallback) {
        String s = lon+","+lat+","+5000;
        Call<PriceResponse> call = mPlaylistService.getFuelTypes(s, type, HEREInfo.API_KEY);
        call.enqueue(new Callback<PriceResponse>() {
            @Override
            public void onResponse(Call<PriceResponse> call, Response<PriceResponse> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    hereCallback.pricesRecieved(response.body());
                } else {
                    try {
                        hereCallback.pricesFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PriceResponse> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                hereCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }
}
