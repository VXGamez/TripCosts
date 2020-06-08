package com.vx.dyvide.model.Michelin;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TollCost {

    @SerializedName("moto")
    @Expose
    private Double moto;
    @SerializedName("car")
    @Expose
    private Double car;
    @SerializedName("caravan")
    @Expose
    private Double caravan;

    public Double getMoto() {
        return moto;
    }

    public void setMoto(Double moto) {
        this.moto = moto;
    }

    public Double getCar() {
        return car;
    }

    public void setCar(Double car) {
        this.car = car;
    }

    public Double getCaravan() {
        return caravan;
    }

    public void setCaravan(Double caravan) {
        this.caravan = caravan;
    }

}
