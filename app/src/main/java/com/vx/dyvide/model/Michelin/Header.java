package com.vx.dyvide.model.Michelin;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Header {
    @SerializedName("summaries")
    @Expose
    private List<Summary> summaries = null;

    public List<Summary> getSummaries() {
        return summaries;
    }

    public void setSummaries(List<Summary> summaries) {
        this.summaries = summaries;
    }

}
