package com.vx.dyvide.model.Michelin;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DestMapDef {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("size")
    @Expose
    private Size_ size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Size_ getSize() {
        return size;
    }

    public void setSize(Size_ size) {
        this.size = size;
    }

}