package com.vx.dyvide.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PriceResponse {

    @SerializedName("hasMore")
    @Expose
    private Boolean hasMore;
    @SerializedName("fuelStations")
    @Expose
    private List<FuelStation> fuelStation;

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }



}