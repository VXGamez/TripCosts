package com.vx.dyvide.controller.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vx.dyvide.R;
import com.vx.dyvide.controller.callbacks.TollListCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.ViewHolder> {

    private static final String TAG = "PlaylistListAdapter";
    private ArrayList<HashMap<String, String>> questions;
    private Context mContext;
    private TollListCallback mCallback;


    public FAQAdapter(Context context, ArrayList<HashMap<String, String>>vehicles) {
        this.questions = vehicles;
        this.mContext = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new FAQAdapter.ViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.question.setText(this.questions.get(position).get("q"));
        holder.answer.setText(this.questions.get(position).get("a"));
    }

    @Override
    public int getItemCount() {
        return questions != null ? questions.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView answer;
        TextView question;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            answer = itemView.findViewById(R.id.answer);
            question= itemView.findViewById(R.id.question);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
