package com.vx.dyvide.model.Michelin;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Header {

    @SerializedName("vehicle")
    @Expose
    private Integer vehicle;
    @SerializedName("itiType")
    @Expose
    private Integer itiType;
    @SerializedName("idx")
    @Expose
    private Integer idx;
    @SerializedName("itidate")
    @Expose
    private String itidate;
    @SerializedName("startMapDef")
    @Expose
    private StartMapDef startMapDef;
    @SerializedName("destMapDef")
    @Expose
    private DestMapDef destMapDef;
    @SerializedName("summaries")
    @Expose
    private List<Summary> summaries = null;

    public Integer getVehicle() {
        return vehicle;
    }

    public void setVehicle(Integer vehicle) {
        this.vehicle = vehicle;
    }

    public Integer getItiType() {
        return itiType;
    }

    public void setItiType(Integer itiType) {
        this.itiType = itiType;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getItidate() {
        return itidate;
    }

    public void setItidate(String itidate) {
        this.itidate = itidate;
    }

    public StartMapDef getStartMapDef() {
        return startMapDef;
    }

    public void setStartMapDef(StartMapDef startMapDef) {
        this.startMapDef = startMapDef;
    }

    public DestMapDef getDestMapDef() {
        return destMapDef;
    }

    public void setDestMapDef(DestMapDef destMapDef) {
        this.destMapDef = destMapDef;
    }

    public List<Summary> getSummaries() {
        return summaries;
    }

    public void setSummaries(List<Summary> summaries) {
        this.summaries = summaries;
    }

}
