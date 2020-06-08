package com.vx.dyvide.model.Michelin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Summary {

    @SerializedName("totalDist")
    @Expose
    private Double totalDist;
    @SerializedName("totalTime")
    @Expose
    private Double totalTime;
    @SerializedName("motorwayDist")
    @Expose
    private Double motorwayDist;
    @SerializedName("motorwayTime")
    @Expose
    private Double motorwayTime;
    @SerializedName("drivingDist")
    @Expose
    private Double drivingDist;
    @SerializedName("drivingTime")
    @Expose
    private Double drivingTime;
    @SerializedName("consumption")
    @Expose
    private Double consumption;
    @SerializedName("carbonFootprintEstimation")
    @Expose
    private Double carbonFootprintEstimation;
    @SerializedName("tollCost")
    @Expose
    private TollCost tollCost;


    public Double getTotalDist() {
        return totalDist;
    }

    public void setTotalDist(Double totalDist) {
        this.totalDist = totalDist;
    }

    public Double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Double totalTime) {
        this.totalTime = totalTime;
    }

    public Double getMotorwayDist() {
        return motorwayDist;
    }

    public void setMotorwayDist(Double motorwayDist) {
        this.motorwayDist = motorwayDist;
    }

    public Double getMotorwayTime() {
        return motorwayTime;
    }

    public void setMotorwayTime(Double motorwayTime) {
        this.motorwayTime = motorwayTime;
    }


    public Double getDrivingDist() {
        return drivingDist;
    }

    public void setDrivingDist(Double drivingDist) {
        this.drivingDist = drivingDist;
    }

    public Double getDrivingTime() {
        return drivingTime;
    }

    public void setDrivingTime(Double drivingTime) {
        this.drivingTime = drivingTime;
    }

    public Double getConsumption() {
        return consumption;
    }

    public void setConsumption(Double consumption) {
        this.consumption = consumption;
    }

    public Double getCarbonFootprintEstimation() {
        return carbonFootprintEstimation;
    }

    public void setCarbonFootprintEstimation(Double carbonFootprintEstimation) {
        this.carbonFootprintEstimation = carbonFootprintEstimation;
    }

    public TollCost getTollCost() {
        return tollCost;
    }

    public void setTollCost(TollCost tollCost) {
        this.tollCost = tollCost;
    }


}