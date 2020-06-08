package com.vx.dyvide.model.Michelin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Example {

    @SerializedName("iti")
    @Expose
    private Iti iti;

    public Iti getIti() {
        return iti;
    }

    public void setIti(Iti iti) {
        this.iti = iti;
    }

}

