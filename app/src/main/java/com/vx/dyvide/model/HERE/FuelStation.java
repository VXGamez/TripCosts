package com.vx.dyvide.model.HERE;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FuelStation {

    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("fuelPrice")
    @Expose
    private List<FuelPrice> fuelPrice;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<FuelPrice> getFuelPrice() {
        return fuelPrice;
    }

    public void setFuelPrice(List<FuelPrice> fuelPrice) {
        this.fuelPrice = fuelPrice;
    }
}
