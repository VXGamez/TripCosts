package com.vx.dyvide.model.Michelin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resposta {
    @SerializedName("iti")
    @Expose
    private Iti iti;

    public Iti getHeader() {
        return iti;
    }

    public void setHeader(Iti header) {
        this.iti = header;
    }

}
