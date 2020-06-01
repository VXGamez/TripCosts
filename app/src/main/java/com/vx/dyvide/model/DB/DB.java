package com.vx.dyvide.model.DB;

import com.vx.dyvide.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class DB {

    public static boolean hasConfig(){
        List<SavedConfig> list = ObjectBox.get().boxFor(SavedConfig.class).query().equal(SavedConfig_.id, 1).build().find();
        return list.size() ==1;
    }

    public static void addVehicle(Vehicle v){
        SavedConfig c = ObjectBox.get().boxFor(SavedConfig.class).get(1);
        ArrayList<Vehicle> veh = c.retrieveVehicles();
        veh.add(v);
        c.saveVehicles(veh);
        ObjectBox.get().boxFor(SavedConfig.class).put(c);
    }

    public static boolean hasCars(){
        return ObjectBox.get().boxFor(SavedConfig.class).get(1).retrieveVehicles().size()>1;
    }

    public static ArrayList<Vehicle> getVehicles(){
        ArrayList<Vehicle> cars = ObjectBox.get().boxFor(SavedConfig.class).get(1).retrieveVehicles();
        Vehicle v = cars.get(0);
        cars.remove(0);
        cars.add(v);
        return cars;
    }

    public static void createConfig(){
        SavedConfig c = new SavedConfig();
        c.setId(1);
        c.selectedVehicle = -3;
        Vehicle v = new Vehicle(-5, "Add", 0, -3);
        ArrayList<Vehicle> array = new ArrayList<>();
        array.add(v);
        c.saveVehicles(array);
        ObjectBox.get().boxFor(SavedConfig.class).put(c);
    }

}
