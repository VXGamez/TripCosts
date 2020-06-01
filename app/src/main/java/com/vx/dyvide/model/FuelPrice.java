package com.vx.dyvide.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FuelPrice {

    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("deltaPrice")
    @Expose
    private Double deltaPrice;
    @SerializedName("indexScore")
    @Expose
    private Integer indexScore;
    @SerializedName("fuelType")
    @Expose
    private String fuelType;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("lastUpdateTimestamp")
    @Expose
    private String lastUpdateTimestamp;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDeltaPrice() {
        return deltaPrice;
    }

    public void setDeltaPrice(Double deltaPrice) {
        this.deltaPrice = deltaPrice;
    }

    public Integer getIndexScore() {
        return indexScore;
    }

    public void setIndexScore(Integer indexScore) {
        this.indexScore = indexScore;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

}