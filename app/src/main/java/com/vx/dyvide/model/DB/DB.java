package com.vx.dyvide.model.DB;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    public static void makeCustomToast(Context c, String ok){
        Toast toast = Toast.makeText(c, ok, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.parseColor("#7ED31F"), PorterDuff.Mode.SRC_IN);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        text.setTypeface(text.getTypeface(), Typeface.BOLD);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 570);
        toast.show();
    }

    public static boolean noInternet(Context c){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return !(activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    public static Vehicle getCurrentVehicle(){
        ArrayList<Vehicle> cars = getVehicles();
        return cars.get(ObjectBox.get().boxFor(SavedConfig.class).get(1).getSelectedVehicle());
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
        c.setOnboard(0);
        c.selectedVehicle = -3;
        Vehicle v = new Vehicle(-5, "Add", 0, -3);
        ArrayList<Vehicle> array = new ArrayList<>();
        array.add(v);
        c.saveVehicles(array);
        ObjectBox.get().boxFor(SavedConfig.class).put(c);
    }

    public static boolean isOnboard() {
        int on = ObjectBox.get().boxFor(SavedConfig.class).get(1).isOnboard;
        return on==1;
    }
}
