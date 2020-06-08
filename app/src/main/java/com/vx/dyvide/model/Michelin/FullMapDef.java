package com.vx.dyvide.model.Michelin;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FullMapDef {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("size")
    @Expose
    private Size__ size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Size__ getSize() {
        return size;
    }

    public void setSize(Size__ size) {
        this.size = size;
    }

}
