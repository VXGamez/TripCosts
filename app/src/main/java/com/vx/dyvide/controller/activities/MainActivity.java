package com.vx.dyvide.controller.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.vx.dyvide.R;
import com.vx.dyvide.model.DB.DB;
import com.vx.dyvide.model.DB.ObjectBox;

import io.objectbox.android.AndroidObjectBrowser;
import io.objectbox.android.BuildConfig;

public class MainActivity extends AppCompatActivity {

    private ImageButton config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObjectBox.init(getApplicationContext());

        boolean started = new AndroidObjectBrowser(ObjectBox.get()).start(this);
        Log.i("ObjectBrowser", "Started: " + started);


        //DB.createConfig();

        if(!DB.hasConfig()){
            DB.createConfig();
        }

        config = findViewById(R.id.settings);
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });



    }
}
