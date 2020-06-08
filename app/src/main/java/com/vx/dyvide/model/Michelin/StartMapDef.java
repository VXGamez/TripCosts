package com.vx.dyvide.model.Michelin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartMapDef {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("size")
    @Expose
    private Size size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

}
