package com.vx.dyvide.model.Michelin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Summary {

    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("names")
    @Expose
    private List<String> names = null;
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
    @SerializedName("pleasantDist")
    @Expose
    private Double pleasantDist;
    @SerializedName("pleasantTime")
    @Expose
    private Double pleasantTime;
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
    @SerializedName("CCZCost")
    @Expose
    private CCZCost cCZCost;
    @SerializedName("fullMapDef")
    @Expose
    private FullMapDef fullMapDef;
    @SerializedName("ecoTax")
    @Expose
    private Double ecoTax;
    @SerializedName("ecoTaxDist")
    @Expose
    private Double ecoTaxDist;
    @SerializedName("ecoTaxRepercussionRate")
    @Expose
    private Double ecoTaxRepercussionRate;
    @SerializedName("distanceByCountry")
    @Expose
    private List<Object> distanceByCountry = null;
    @SerializedName("avoidClosedRoadUsed")
    @Expose
    private Boolean avoidClosedRoadUsed;
    @SerializedName("eventTrafficDatabaseAvailable")
    @Expose
    private Boolean eventTrafficDatabaseAvailable;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

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

    public Double getPleasantDist() {
        return pleasantDist;
    }

    public void setPleasantDist(Double pleasantDist) {
        this.pleasantDist = pleasantDist;
    }

    public Double getPleasantTime() {
        return pleasantTime;
    }

    public void setPleasantTime(Double pleasantTime) {
        this.pleasantTime = pleasantTime;
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

    public CCZCost getCCZCost() {
        return cCZCost;
    }

    public void setCCZCost(CCZCost cCZCost) {
        this.cCZCost = cCZCost;
    }

    public FullMapDef getFullMapDef() {
        return fullMapDef;
    }

    public void setFullMapDef(FullMapDef fullMapDef) {
        this.fullMapDef = fullMapDef;
    }

    public Double getEcoTax() {
        return ecoTax;
    }

    public void setEcoTax(Double ecoTax) {
        this.ecoTax = ecoTax;
    }

    public Double getEcoTaxDist() {
        return ecoTaxDist;
    }

    public void setEcoTaxDist(Double ecoTaxDist) {
        this.ecoTaxDist = ecoTaxDist;
    }

    public Double getEcoTaxRepercussionRate() {
        return ecoTaxRepercussionRate;
    }

    public void setEcoTaxRepercussionRate(Double ecoTaxRepercussionRate) {
        this.ecoTaxRepercussionRate = ecoTaxRepercussionRate;
    }

    public List<Object> getDistanceByCountry() {
        return distanceByCountry;
    }

    public void setDistanceByCountry(List<Object> distanceByCountry) {
        this.distanceByCountry = distanceByCountry;
    }

    public Boolean getAvoidClosedRoadUsed() {
        return avoidClosedRoadUsed;
    }

    public void setAvoidClosedRoadUsed(Boolean avoidClosedRoadUsed) {
        this.avoidClosedRoadUsed = avoidClosedRoadUsed;
    }

    public Boolean getEventTrafficDatabaseAvailable() {
        return eventTrafficDatabaseAvailable;
    }

    public void setEventTrafficDatabaseAvailable(Boolean eventTrafficDatabaseAvailable) {
        this.eventTrafficDatabaseAvailable = eventTrafficDatabaseAvailable;
    }

}