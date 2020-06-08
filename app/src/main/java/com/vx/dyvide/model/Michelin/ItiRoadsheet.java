package com.vx.dyvide.model.Michelin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItiRoadsheet {

    @SerializedName("roadSheet")
    @Expose
    private List<List<Object>> roadSheet;

    public List<List<Object>> getRoadSheet() {
        return roadSheet;
    }

    public void setRoadSheet(List<List<Object>> roadSheet) {
        this.roadSheet = roadSheet;
    }
}
