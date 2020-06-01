package com.prpr.androidpprog2.entregable.controller.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vx.dyvide.R;
import com.vx.dyvide.controller.callbacks.VehicleCallback;
import com.vx.dyvide.model.Vehicle;

import java.util.ArrayList;


public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {

    private static final String TAG = "PlaylistListAdapter";
    private ArrayList<Vehicle> vehicles;
    private Context mContext;
    private VehicleCallback mCallback;


    public VehicleAdapter(Context context, ArrayList<Vehicle> vehicles, final VehicleCallback itemClickCallback) {
        this.vehicles = vehicles;
        this.mContext = context;
        this.mCallback = itemClickCallback;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_item, parent, false);
        return new VehicleAdapter.ViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.vehicleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vehicles.get(position).getType()==-5){
                    mCallback.createNewVehicle();
                }else{
                    mCallback.vehiclePressed(vehicles.get(position), position);
                }
            }
        });

        holder.vehicleName.setText(vehicles.get(position).getName());
        if(vehicles.get(position).getType()==1){
            holder.vehicleIcon.setBackgroundResource(R.drawable.coche);
        }else if(vehicles.get(position).getType()==2){
            holder.vehicleIcon.setBackgroundResource(R.drawable.moto);
        }else if(vehicles.get(position).getType()==-5){
            holder.vehicleIcon.setBackgroundResource(R.drawable.add);
        }
    }

    @Override
    public int getItemCount() {
        return vehicles != null ? vehicles.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView vehicleName;
        ImageButton vehicleIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleName = itemView.findViewById(R.id.carName);
            vehicleIcon= itemView.findViewById(R.id.carImage);
        }
    }
}
