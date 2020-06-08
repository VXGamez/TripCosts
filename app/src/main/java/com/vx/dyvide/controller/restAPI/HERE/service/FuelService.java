package com.vx.dyvide.controller.restAPI.HERE.service;

import com.vx.dyvide.model.HERE.PriceResponse;
import com.vx.dyvide.model.HERE.Token;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface FuelService {

    @GET("fuel/stations.json")
    Call<PriceResponse> getFuelTypes(@Query(value="prox", encoded=true) String prox, @Query(value="fueltype", encoded=true) int index, @Query("apiKey") String apiKey);
    //Call<PriceResponse> getFuelTypes(@Header("Authorization") String token, @Query(value="prox", encoded=true) String prox, @Query(value="fueltype", encoded=true) int index);
    @POST()
    Call<Token> requestToken();
}
