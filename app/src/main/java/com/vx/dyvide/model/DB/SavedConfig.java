package com.vx.dyvide.model.DB;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vx.dyvide.model.Vehicle;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class SavedConfig {

    @Id(assignable = true) public long id;
    public int selectedVehicle;
    public boolean hasVehicles;
    public String vehicles;


    public void saveVehicles(ArrayList<Vehicle> coches) {
        Gson gson = new Gson();
        this.vehicles = gson.toJson(coches);
    }

    public ArrayList<Vehicle> retrieveVehicles() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Vehicle>>() {}.getType();
        if(this.vehicles!= null ){
            return gson.fromJson(this.vehicles, type);
        }else{
            return new ArrayList<>();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSelectedVehicle() {
        return selectedVehicle;
    }

    public void setSelectedVehicle(int selectedVehicle) {
        this.selectedVehicle = selectedVehicle;
    }

    public boolean isHasVehicles() {
        return hasVehicles;
    }

    public void setHasVehicles(boolean hasVehicles) {
        this.hasVehicles = hasVehicles;
    }

    public String getVehicles() {
        return vehicles;
    }

    public void setVehicles(String vehicles) {
        this.vehicles = vehicles;
    }
}
