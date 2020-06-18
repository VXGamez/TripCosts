package com.vx.dyvide.controller.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.reflect.TypeToken;
import com.vx.dyvide.R;
import com.vx.dyvide.controller.adapters.FAQAdapter;
import com.vx.dyvide.controller.adapters.TollAdapter;
import com.vx.dyvide.controller.adapters.VehicleAdapter;
import com.vx.dyvide.model.DB.DB;
import com.vx.dyvide.model.DB.ObjectBox;
import com.vx.dyvide.model.DB.SavedConfig;
import com.vx.dyvide.model.Vehicle;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SupportFragment extends Fragment {

    private RecyclerView faqRecycle;
    private Button form;

    public static SupportFragment newInstance(int page, String title) {
        SupportFragment fragmentFirst = new SupportFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DB.setLanguage(getActivity());

        View view = inflater.inflate(R.layout.support_fragment, container, false);

        faqRecycle = view.findViewById(R.id.faqRecycle);
        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        FAQAdapter adapter = new FAQAdapter(getActivity(), null);
        faqRecycle.setLayoutManager(manager2);
        faqRecycle.setAdapter(adapter);

        form = view.findViewById(R.id.supportForm);
        form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSf9QUUaLsaaiMro1W3KlAnakbo_ZlBQS8QLDyCq0Cn279rC-A/viewform?usp=sf_link");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("0/"+ ObjectBox.get().boxFor(SavedConfig.class).get(1).lan +"/0/questions");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        ArrayList<HashMap<String, String>> value = (ArrayList<HashMap<String, String>>) dataSnapshot.getValue();
                        FAQAdapter adapter = new FAQAdapter(getActivity(), value);
                        faqRecycle.setAdapter(adapter);
                        System.out.println();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("", "Failed to read value.", error.toException());
            }
        });


        return view;
    }

}
