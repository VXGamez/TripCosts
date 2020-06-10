package com.vx.dyvide.controller.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.vx.dyvide.R;
import com.vx.dyvide.controller.activities.MainActivity;
import com.vx.dyvide.controller.restAPI.Michelin.callbacks.MichelinCallback;
import com.vx.dyvide.controller.restAPI.Michelin.managers.RouteManager;
import com.vx.dyvide.model.DB.DB;
import com.vx.dyvide.model.DB.ObjectBox;
import com.vx.dyvide.model.DB.SavedConfig;
import com.vx.dyvide.model.HERE.directionhelpers.FetchURL;
import com.vx.dyvide.model.HERE.directionhelpers.TaskLoadedCallback;
import com.vx.dyvide.model.Michelin.Summary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.vx.dyvide.controller.fragments.ManualFragment.round;

public class AutoFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback, MichelinCallback {

    private GoogleMap map;
    private ScrollView scrollView;
    private ImageButton myLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng origin;
    private LatLng destination;
    private EditText originTXT;
    private EditText totalPassengers;
    private TextView totalKM;
    private int totalKMCalculats=0;
    private EditText destinationTXT;
    private Marker originMarker;
    private Marker destinationMarker;

    private RecyclerView tollRecycle;

    private Switch tollSwitch;
    private boolean wantsTolls;
    private LinearLayout tolls;

    private ImageButton swapDestinations;

    private Polyline currentPolyline;
    private Button calculate;

    private void swapDestinations(){
        LatLng tmp = destination;
        destination = origin;
        origin = tmp;
        repaintMap();
    }

    private void repaintMap() {

        if(origin!=null){
            destinationMarker.remove();
            destinationMarker = null;
            originMarker = map.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_opin))));
        }
        if(destination!=null){
            originMarker.remove();
            originMarker = null;
            destinationMarker = map.addMarker(new MarkerOptions().position(destination).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_dpin))));
        }
    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.auto_fragment, container, false);
        tolls = view.findViewById(R.id.tolls);
        tollSwitch = view.findViewById(R.id.tollSwitch);
        tolls.setVisibility(View.GONE);
        tollSwitch.setVisibility(View.INVISIBLE);
        tollSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wantsTolls = isChecked;
                if(isChecked){
                    tolls.setVisibility(View.VISIBLE);
                }else{
                    tolls.setVisibility(View.GONE);
                }
            }
        });
        tollRecycle= view.findViewById(R.id.tollRecycle);
        tollRecycle.setVisibility(View.GONE);
        scrollView = view.findViewById(R.id.statisticsScrollview);
        totalKM = view.findViewById(R.id.totalKM);
        swapDestinations= view.findViewById(R.id.swap);
        swapDestinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(destination==null && origin ==null) {
                    makeCustomToast("Please setup origin or destination first");
                }else{
                    swapDestinations();
                    String tmpString = destinationTXT.getText().toString();
                    destinationTXT.setText(originTXT.getText().toString());
                    originTXT.setText(tmpString);
                }
            }
        });

        totalKM.setText("Total distance: 0 km");
        totalPassengers = view.findViewById(R.id.totalPassengers);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        originTXT= view.findViewById(R.id.originTXT);
        originTXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(destination==null){
                    map.clear();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        originTXT.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    geoLocate(true);
                }
                return false;
            }
        });
        destinationTXT= view.findViewById(R.id.destinationTXT);
        destinationTXT.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    geoLocate(false);
                }
                return false;
            }
        });
        destinationTXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(origin==null){
                    map.clear();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        myLocation = view.findViewById(R.id.myLocation);
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOriginCurrentLocation();
                originTXT.setText("My Location");
                updateMapZoom();
            }
        });


        calculate = view.findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ok = "";
                if(totOK()){
                    if(DB.hasCars()){
                        float total = calculateTotalCost();
                        ok = round(total, 2) + "€ x Pers.";
                        //totalCost.setVisibility(View.VISIBLE);
                        //totalCost.setText("Total: "+ ok);
                    }else{
                        ok = "Please setup a car!";
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(50);
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        ((MainActivity)getActivity()).getConfig().startAnimation(anim);
                    }

                }else{
                    ok = "Non-valid values. Please fill again";
                }
                makeCustomToast(ok);
            }
        });

        Rect scrollBounds = new Rect();
        scrollView.getHitRect(scrollBounds);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageView ivMapTransparent = (ImageView) view.findViewById(R.id.ivMapTransparent);
        ivMapTransparent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

        return view;
    }

    private float calculateTotalCost() {
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
        totalCost = ((consum/100)*metersToKM()*fuelCost);
        //if(wantsTolls){
        //    totalCost += Float.parseFloat(totalTolls.getText().toString());
        //}
        return totalCost/Integer.parseInt(totalPassengers.getText().toString());
    }

    private float metersToKM() {
        return totalKMCalculats/1000;
    }

    private boolean totOK() {
        boolean ok = true;
        if(origin==null){
            ok = false;
        }else if(destination==null){
            ok = false;
        }else if(totalPassengers.getText().equals("")){
            ok = false;
        }else if(totalKMCalculats == 0){
            ok = false;
        }else if(Float.parseFloat(totalPassengers.getText().toString())<=0 || Float.parseFloat(totalPassengers.getText().toString())>=10){
            ok = false;
        }

        return ok;
    }

    private void updateMapZoom(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        int padding = 0;
        if(origin!=null){
            builder.include(origin);
            if(padding>0){
                padding=200;
            }else{
                padding=120;
            }
        }
        if(destination!=null){
            builder.include(destination);
            if(padding>0){
                padding=200;
            }else{
                padding=120;
            }
        }

        if(origin!=null || destination!=null){
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            map.animateCamera(cu);
            if(origin!=null && destination!=null){
                hideKeyboard(getActivity());
                RouteManager.getInstance(this).getRouteHeader(origin, destination, getVehicleType(), DB.getCurrentVehicle().getConsum(), 1.48f, this);
                drawRoute();
            }
        }

    }

    private void makeRecycle(){

    }

    private int getVehicleType() {
        return 0;
    }


    public void setOriginCurrentLocation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                origin = loc;
                originMarker = map.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_opin))));
                updateMapZoom();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        }
    }

    private void geoLocate(boolean isOrigin){

        String searchString = destinationTXT.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e("Location", "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);
            LatLng loc = new LatLng(address.getLatitude(), address.getLongitude());

            if(isOrigin){
                origin = loc;
                originMarker = map.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_opin))));
            }else{
                destination = loc;
                destinationMarker = map.addMarker(new MarkerOptions().position(destination).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_dpin))));
            }


            updateMapZoom();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });

        try {

            boolean success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_style));

        } catch (Resources.NotFoundException e) {

        }
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.resetMinMaxZoomPreference();
        map.setMaxZoomPreference(16.0f);
    }

    private void drawRoute() {
        new FetchURL(this).execute(getUrl(origin, destination, "driving"), "driving");
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    private void makeCustomToast(String ok){
        Toast toast = Toast.makeText(getActivity(), ok, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.parseColor("#7ED31F"), PorterDuff.Mode.SRC_IN);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        text.setTypeface(text.getTypeface(), Typeface.BOLD);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 570);
        toast.show();
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void totalValues(String totalKM, int totalDistanceValue, String totalDuration, int totalDurationValue) {
        this.totalKM.setText("Total distance: " + totalKM);
        this.totalKMCalculats = totalDistanceValue;
    }


    @Override
    public void onHeaderRecieved(Summary body) {
        if(body.getTollCost().getCar()>0){
            makeCustomToast("This route has tolls");
            tollSwitch.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onHeaderFailure(Throwable throwable) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onRoadSheetRecieved(ArrayList<Object> parseJsonRoadsheet) {

    }

    @Override
    public void onRoadSheetFailure(Throwable throwable) {

    }
}
