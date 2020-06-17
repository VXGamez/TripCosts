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
    public int isOnboard;
    public String vehicles;
    public String lan;

    public int getIsOnboard() {
        return isOnboard;
    }

    public void setIsOnboard(int isOnboard) {
        this.isOnboard = isOnboard;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public int isOnboard() {
        return isOnboard;
    }

    public void setOnboard(int onboard) {
        isOnboard = onboard;
    }

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


    public String getVehicles() {
        return vehicles;
    }

    public void setVehicles(String vehicles) {
        this.vehicles = vehicles;
    }
}
