package com.vx.dyvide.controller.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.vx.dyvide.controller.fragments.SupportFragment;
import com.vx.dyvide.controller.onboarding.OnboardingActivity;
import com.vx.dyvide.controller.restAPI.HERE.callbacks.HereCallback;
import com.vx.dyvide.controller.restAPI.Michelin.callbacks.MichelinCallback;
import com.vx.dyvide.controller.restAPI.Michelin.managers.RouteManager;
import com.vx.dyvide.controller.service.ConnectivityService;
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
    private TextView support;
    private static SpringAnimation springAnimation;
    private static int width;
    private boolean inicialized = false;
    private boolean hasInternet = true;
    private boolean needsOnboard = true;
    private Fragment menu;
    private ViewPager vpPager;
    private AdView mAdView;

    private FragmentPagerAdapter adapterViewPager;



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectionLost);
        unregisterReceiver(connectionRegained);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 131) {
            if (resultCode == RESULT_OK) {
                String onboard = data.getStringExtra("onboard");
                if(onboard.equals("TRUE")){
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }
            }
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AutoFragment.newInstance(0, "Auto Route");
                case 1:
                    return ManualFragment.newInstance(1, "Manual");
                case 2:
                    return SupportFragment.newInstance(2, "Support");
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObjectBox.init(getApplicationContext());
        getScreenSize();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Intent intent = new Intent(this, ConnectivityService.class);
        intent.putExtra(ConnectivityService.TAG_INTERVAL, 3);
        startService(intent);

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    springAnimation.animateToFinalPosition(20);
                }else if(position==1){
                    springAnimation.animateToFinalPosition(20+(width/3));
                }else if(position==2){
                    springAnimation.animateToFinalPosition(20+(width/3)*2);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mAdView = findViewById(R.id.adView);

        if(!DB.hasConfig()){
            DB.createConfig();
            Intent intento = new Intent(this, OnboardingActivity.class);
            startActivityForResult(intento, 131);
            overridePendingTransition(R.anim.nothing, R.anim.nothing);
        }else{
            if(!DB.hasCars()){
                Intent intent2 = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                DB.makeCustomToast(this, "Please setup a vehicle");
            }
        }

        menu = getSupportFragmentManager().findFragmentById(R.id.user_menu);

        config = findViewById(R.id.settings);
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });



        registerConnectionRegained();
        registerConnectionLost();

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
                if(hasInternet){
                    initAuto();
                }else{
                    DB.makeCustomToast(MainActivity.this,"Not available without internet");
                }
            }
        });

        manual = (TextView) findViewById(R.id.manualRoute);
        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initManual();
            }
        });
        support = (TextView) findViewById(R.id.support);
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSupport();
            }
        });

        if(DB.noInternet(this)){
            hasInternet = false;
            initManual();
        }else{
            inicialitzarInternete();
            inicialized = true;
        }
    }

    private void inicialitzarInternete() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String apiKey = getString(R.string.google_maps_key);


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
    }

    private void registerConnectionRegained() {
        IntentFilter filter = new IntentFilter(ConnectivityService.Broadcast_CONNECTION_REGAINED);
        registerReceiver(connectionRegained, filter);
    }

    private void registerConnectionLost() {
        IntentFilter filter = new IntentFilter(ConnectivityService.Broadcast_CONNECTION_LOST);
        registerReceiver(connectionLost, filter);
    }


    private BroadcastReceiver connectionRegained = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hasInternet = true;
            if(!inicialized){
                inicialitzarInternete();
            }
        }
    };


    private BroadcastReceiver connectionLost = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hasInternet = false;
            initManual();
        }
    };


    void getScreenSize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
    }

    private  void initManual() {
        vpPager.setCurrentItem(1);
        springAnimation.animateToFinalPosition(20+(width/3));

    }

    private  void initSupport() {
        vpPager.setCurrentItem(2);
        springAnimation.animateToFinalPosition(20+(width/3)*2);
    }

    private void initAuto(){

        springAnimation.animateToFinalPosition(20);
        vpPager.setCurrentItem(0);

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
