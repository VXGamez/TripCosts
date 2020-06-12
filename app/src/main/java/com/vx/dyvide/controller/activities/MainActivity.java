package com.vx.dyvide.controller.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.vx.dyvide.R;
import com.vx.dyvide.controller.fragments.AutoFragment;
import com.vx.dyvide.controller.fragments.ManualFragment;
import com.vx.dyvide.controller.restAPI.HERE.callbacks.HereCallback;
import com.vx.dyvide.controller.restAPI.Michelin.callbacks.MichelinCallback;
import com.vx.dyvide.controller.restAPI.Michelin.managers.RouteManager;
import com.vx.dyvide.model.DB.DB;
import com.vx.dyvide.model.DB.ObjectBox;
import com.vx.dyvide.model.HERE.PriceResponse;
import com.vx.dyvide.model.Michelin.Iti;
import com.vx.dyvide.model.Michelin.ItiRoadsheet;
import com.vx.dyvide.model.Michelin.RoadSheet;
import com.vx.dyvide.model.Michelin.Summary;

import java.util.ArrayList;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements HereCallback {

    private ImageButton config;
    private View bigView;
    private TextView auto;
    private TextView manual;
    private SpringAnimation springAnimation;
    private int width;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObjectBox.init(getApplicationContext());
        getScreenSize();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String apiKey = getString(R.string.google_maps_key);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

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


       bigView = (View) findViewById(R.id.view_big_bar);

        SpringForce springForce = new SpringForce(0).setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY)
                .setStiffness(SpringForce.STIFFNESS_MEDIUM);

        springAnimation = new SpringAnimation(bigView, DynamicAnimation.TRANSLATION_X).setSpring(springForce);
        springAnimation.animateToFinalPosition(130);
        auto = (TextView) findViewById(R.id.autoRoute);
        initAuto();
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initAuto();
            }
        });

        manual = (TextView) findViewById(R.id.manualRoute);
        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initManual();
            }
        });

    }



    void getScreenSize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.width = size.x;
    }

    private void initManual() {
        springAnimation.animateToFinalPosition(130+this.width/2);
        ManualFragment manualFragment = new ManualFragment();

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.typeLayout, manualFragment, manualFragment.getTag())
                .commit();
    }

    void initAuto(){
        springAnimation.animateToFinalPosition(130);
        AutoFragment autoFragment = new AutoFragment();

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.typeLayout, autoFragment, autoFragment.getTag())
                .commit();

    }

    public ImageButton getConfig() {
        return config;
    }

    @Override
    public void pricesRecieved(PriceResponse body) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void pricesFailure(Throwable throwable) {

    }
}
