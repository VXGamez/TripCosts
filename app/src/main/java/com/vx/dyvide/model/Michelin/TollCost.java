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
    @SerializedName("pl2")
    @Expose
    private Double pl2;
    @SerializedName("pl3")
    @Expose
    private Double pl3;
    @SerializedName("pl4")
    @Expose
    private Double pl4;
    @SerializedName("pl5")
    @Expose
    private Double pl5;

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

    public Double getPl2() {
        return pl2;
    }

    public void setPl2(Double pl2) {
        this.pl2 = pl2;
    }

    public Double getPl3() {
        return pl3;
    }

    public void setPl3(Double pl3) {
        this.pl3 = pl3;
    }

    public Double getPl4() {
        return pl4;
    }

    public void setPl4(Double pl4) {
        this.pl4 = pl4;
    }

    public Double getPl5() {
        return pl5;
    }

    public void setPl5(Double pl5) {
        this.pl5 = pl5;
    }

}
