package com.vx.dyvide.controller.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vx.dyvide.R;
import com.vx.dyvide.controller.callbacks.TollListCallback;
import com.vx.dyvide.model.Vehicle;

import java.util.ArrayList;

public class TollAdapter extends RecyclerView.Adapter<TollAdapter.ViewHolder> {

    private static final String TAG = "PlaylistListAdapter";
    private ArrayList<String> peajes;
    private Context mContext;
    private TollListCallback mCallback;


    public TollAdapter(Context context, ArrayList<String> vehicles, final TollListCallback itemClickCallback) {
        this.peajes = vehicles;
        this.mContext = context;
        this.mCallback = itemClickCallback;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.toll_item, parent, false);
        return new TollAdapter.ViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tollName.setText(this.peajes.get(position));
        holder.checkBox.setChecked(true);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCallback.tollSelected(peajes.get(position), isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return peajes != null ? peajes.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView tollName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            tollName= itemView.findViewById(R.id.tollName);
        }
    }
}
