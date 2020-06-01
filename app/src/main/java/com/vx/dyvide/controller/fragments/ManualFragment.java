package com.vx.dyvide.controller.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.vx.dyvide.R;
import com.vx.dyvide.controller.activities.SettingsActivity;
import com.vx.dyvide.model.DB.DB;
import com.vx.dyvide.model.DB.ObjectBox;
import com.vx.dyvide.model.DB.SavedConfig;
import com.vx.dyvide.model.Vehicle;

import java.util.ArrayList;

public class ManualFragment  extends Fragment {

    private EditText totalKM;
    private EditText totalPassengers;
    private Button calculate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.manual_fragment, container, false);

        totalKM = view.findViewById(R.id.totalKM);
        totalPassengers = view.findViewById(R.id.totalPassengers);
        calculate = view.findViewById(R.id.calculate);
        calculate.setEnabled(true);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ok= "";
                if(totOk()){
                    Double total = calculateTotalCost();
                    ok = round(total, 2) + " x Pers";
                }else{
                    ok = "Please fill everything";
                }
                Toast toast = Toast.makeText(getActivity(), ok, Toast.LENGTH_SHORT);
                View view = toast.getView();
                view.getBackground().setColorFilter(Color.parseColor("#7ED31F"), PorterDuff.Mode.SRC_IN);
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(Color.WHITE);
                text.setTypeface(text.getTypeface(), Typeface.BOLD);
                toast.show();

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

    private Double calculateTotalCost(){
        Double totalCost;
        float consum = DB.getVehicles().get(ObjectBox.get().boxFor(SavedConfig.class).get(1).selectedVehicle).getConsum();
        totalCost = (((consum/100)*Integer.parseInt(totalKM.getText().toString()))*1.101)/Integer.parseInt(totalPassengers.getText().toString());
        return totalCost;
    }

}
