package com.vx.dyvide.controller.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.vx.dyvide.R;
import com.vx.dyvide.model.DB.DB;
import com.vx.dyvide.model.DB.ObjectBox;
import com.vx.dyvide.model.DB.SavedConfig;

public class ManualFragment  extends Fragment {

    private EditText totalKM;
    private EditText totalTolls;
    private EditText totalPassengers;
    private Button calculate;
    private Switch peatges;
    private boolean wantsTolls;
    private LinearLayout tolls;

    private TextView totalCost;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.manual_fragment, container, false);
        tolls = view.findViewById(R.id.tolls);
        tolls.setVisibility(View.INVISIBLE);
        totalCost = view.findViewById(R.id.totalCost);
        totalCost.setVisibility(View.INVISIBLE);
        totalTolls = view.findViewById(R.id.tollCost);
        peatges = view.findViewById(R.id.tollSwitch);
        peatges.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wantsTolls = isChecked;
                if(isChecked){
                    tolls.setVisibility(View.VISIBLE);
                }else{
                    tolls.setVisibility(View.INVISIBLE);
                    totalTolls.setText("");
                }
            }
        });

        totalKM = view.findViewById(R.id.totalKM);
        totalPassengers = view.findViewById(R.id.totalPassengers);
        calculate = view.findViewById(R.id.calculate);
        calculate.setEnabled(true);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ok= "";
                if(totOk()){
                    if(DB.hasCars()){
                        float total = calculateTotalCost();
                        ok = round(total, 2) + "€ x Pers.";
                        totalCost.setVisibility(View.VISIBLE);
                        totalCost.setText("Total: "+ ok);
                    }else{
                        ok = "Please setup a car!";
                    }

                }else{
                    ok = "Non-valid values. Please fill again";
                }
                DB.makeCustomToast(getActivity(), ok);
            }
        });

        return view;
    }

    private boolean totOk() {
        boolean ok = true;
        if(totalPassengers.getText().toString().equals("")){
            ok = false;
        }else if(totalKM.getText().toString().equals("")){
            ok = false;
        }else if(Float.parseFloat(totalKM.getText().toString())<=0){
            ok = false;
        }else if(Float.parseFloat(totalPassengers.getText().toString())<=0 || Float.parseFloat(totalPassengers.getText().toString())>=10){
            ok = false;
        }else if(wantsTolls){
            if(Float.parseFloat(totalTolls.getText().toString())<=0){
                ok = false;
            }
        }
        return ok;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private float calculateTotalCost(){
        float totalCost;
        float consum = DB.getVehicles().get(ObjectBox.get().boxFor(SavedConfig.class).get(1).selectedVehicle).getConsum();
        float fuelCost = 0;
        float fuelType = DB.getVehicles().get(ObjectBox.get().boxFor(SavedConfig.class).get(1).selectedVehicle).getFuel();
        if(fuelType == 0){
            //diesel
            fuelCost = (float) 1.002;
        }else if(fuelType == 1){
            //gasolina
            fuelCost = (float) 1.112;
        }else if(fuelType == 2){
            //electric
            fuelCost = -1;
        }
        totalCost = (((consum/100)*Integer.parseInt(totalKM.getText().toString()))*fuelCost);
        if(wantsTolls){
            totalCost += Float.parseFloat(totalTolls.getText().toString());
        }
        return totalCost/Integer.parseInt(totalPassengers.getText().toString());
    }

}
