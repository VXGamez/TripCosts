package com.vx.dyvide.controller.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import android.view.ViewTreeObserver;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.vx.dyvide.R;
import com.vx.dyvide.controller.activities.MainActivity;
import com.vx.dyvide.controller.adapters.TollAdapter;
import com.vx.dyvide.controller.adapters.VehicleAdapter;
import com.vx.dyvide.controller.callbacks.TollListCallback;
import com.vx.dyvide.controller.dialogs.ErrorDialog;
import com.vx.dyvide.controller.dialogs.LoadingDialog;
import com.vx.dyvide.controller.dialogs.PriceDialog;
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
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vx.dyvide.controller.fragments.ManualFragment.round;

public class AutoFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback, MichelinCallback, TollListCallback {

    private GoogleMap map;
    private ScrollView scrollView;
    private ImageButton myLocationOrigin;
    private ImageButton myLocationDestination;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng origin;
    private LatLng destination;

    public static AutocompleteSupportFragment autocompleteOrigin;
    public static AutocompleteSupportFragment autocompleteDestination;
    private ImageButton clearButtonOrigin;
    private ImageButton clearButtonDestination;


    private EditText totalPassengers;
    private TextView totalKM;
    private int totalKMCalculats=0;
    private Marker originMarker;
    private Marker destinationMarker;

    private Double totalTollCost;

    private RecyclerView tollRecycle;
    private TextView totalTollCostTXT;

    private TextView totalTripCostTXT;
    private float totalTripCost;

    private boolean infoRecieved=false;

    private Switch tollSwitch;
    private boolean wantsTolls;
    private LinearLayout tolls;

    private ImageButton swapDestinations;

    private Polyline currentPolyline;
    private Button calculate;


    public static AutoFragment newInstance(int page, String title) {
        AutoFragment fragmentFirst = new AutoFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    private void swapDestinations(){
        LatLng tmp = destination;
        destination = origin;
        origin = tmp;
        EditText textDest = autocompleteDestination.getView().findViewById(R.id.places_autocomplete_search_input);
        EditText textOrig = autocompleteOrigin.getView().findViewById(R.id.places_autocomplete_search_input);
        String sTmp = textDest.getText().toString();
        textDest.setText(textOrig.getText().toString());
        textOrig.setText(sTmp);
        if(!textDest.getText().toString().equals("")  &&  !textDest.getText().toString().isEmpty()){
            clearButtonDestination.setVisibility(View.VISIBLE);
        }else{
            clearButtonDestination.setVisibility(View.GONE);
        }

        if(!textOrig.getText().toString().equals("")  &&  !textOrig.getText().toString().isEmpty()){
            clearButtonOrigin.setVisibility(View.VISIBLE);
        }else{
            clearButtonOrigin.setVisibility(View.GONE);
        }
        repaintMap();
    }

    private void repaintMap() {

        if(originMarker!=null){
            originMarker.remove();
            originMarker = null;
        }
        if(destinationMarker!=null){
            destinationMarker.remove();
            destinationMarker = null;
        }

        originMarker = map.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_opin))));
        destinationMarker = map.addMarker(new MarkerOptions().position(destination).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_dpin))));

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

        autocompleteOrigin = (AutocompleteSupportFragment) this.getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteOrigin.setPlaceFields(Arrays.asList(Place.Field.ID,
                Place.Field.NAME,Place.Field.LAT_LNG));
        View fView = autocompleteOrigin.getView();
        EditText etTextInput = fView.findViewById(R.id.places_autocomplete_search_input);
        etTextInput.setTextColor(Color.WHITE);
        etTextInput.setHint("Origin");
        etTextInput.setBackgroundResource(R.drawable.textfield);
        etTextInput.setHintTextColor(Color.WHITE);
        etTextInput.setTextSize(12.5f);
        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.montserrat_regular);
        etTextInput.setTypeface(typeface);


        fView.findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
        ImageButton clearOrigin= fView.findViewById(R.id.places_autocomplete_clear_button);
        clearOrigin.setTag(clearOrigin.getVisibility());
        clearOrigin.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(clearOrigin.getVisibility()==View.VISIBLE){
                    clearButtonOrigin.setVisibility(View.VISIBLE);
                }
                clearOrigin.setVisibility(View.GONE);
            }
        });
        clearButtonOrigin = view.findViewById(R.id.clearOrigin);
        clearButtonOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etTextInput.setText("");
                if(currentPolyline!=null){
                    currentPolyline.remove();
                    currentPolyline=null;
                }
                resetValues();
                originMarker.remove();
                origin = null;
                clearButtonOrigin.setVisibility(View.GONE);
            }
        });
        autocompleteOrigin.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if(originMarker!=null){
                    originMarker.remove();
                    originMarker = null;
                }
                origin = place.getLatLng();
                originMarker = map.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_opin))));
                updateMapZoom();
            }

            @Override
            public void onError(Status status) {
                System.out.println();
            }
        });


        autocompleteDestination = (AutocompleteSupportFragment) this.getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_destination);

        autocompleteDestination.setPlaceFields(Arrays.asList(Place.Field.ID,
                Place.Field.NAME,Place.Field.LAT_LNG));
        View fViewD = autocompleteDestination.getView();
        EditText etTextInputD = fViewD.findViewById(R.id.places_autocomplete_search_input);
        etTextInputD.setTextColor(Color.WHITE);
        etTextInputD.setHint("Destination");
        etTextInputD.setBackgroundResource(R.drawable.textfield);
        etTextInputD.setHintTextColor(Color.WHITE);
        etTextInputD.setTextSize(12.5f);
        etTextInputD.setTypeface(typeface);



        fViewD.findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
        ImageButton clearOriginD = fViewD.findViewById(R.id.places_autocomplete_clear_button);
        clearOriginD.setTag(clearOriginD.getVisibility());
        clearOriginD.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(clearOriginD.getVisibility()==View.VISIBLE){
                    clearButtonDestination.setVisibility(View.VISIBLE);
                }
                clearOriginD.setVisibility(View.GONE);
            }
        });
        clearButtonDestination = view.findViewById(R.id.clearDestination);
        clearButtonDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etTextInputD.setText("");
                resetValues();
                if(currentPolyline!=null){
                    currentPolyline.remove();
                    currentPolyline=null;
                }
                destinationMarker.remove();
                destination = null;
                clearButtonDestination.setVisibility(View.GONE);
            }
        });
        autocompleteDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if(destinationMarker!=null){
                    destinationMarker.remove();
                    destinationMarker = null;
                }
                destination = place.getLatLng();
                destinationMarker = map.addMarker(new MarkerOptions().position(destination).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_dpin))));
                updateMapZoom();
            }

            @Override
            public void onError(Status status) {

            }
        });


        tolls = view.findViewById(R.id.tolls);
        tollSwitch = view.findViewById(R.id.tollSwitch);
        totalTripCostTXT = view.findViewById(R.id.costTotalTrajetcte);
        tollRecycle = (RecyclerView) view.findViewById(R.id.tollRecycle);
        tolls.setVisibility(View.GONE);
        tollSwitch.setVisibility(View.INVISIBLE);
        totalTollCostTXT = view.findViewById(R.id.totalTollCost);
        tollSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wantsTolls = isChecked;
                if(isChecked){
                    tolls.setVisibility(View.VISIBLE);
                    tollRecycle.setVisibility(View.VISIBLE);
                    totalTripCost += totalTollCost;
                }else{
                    tolls.setVisibility(View.GONE);
                    tollRecycle.setVisibility(View.GONE);
                    totalTripCost -= totalTollCost;
                }
                updateTotalTripCost();
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
                    DB.makeCustomToast(getActivity(),"Please setup origin or destination first");
                }else{
                    swapDestinations();
                }
            }
        });

        totalKM.setText("Total distance: 0 km");
        totalPassengers = view.findViewById(R.id.totalPassengers);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());


        myLocationOrigin = view.findViewById(R.id.myLocationOrigin);
        myLocationOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DB.isLocationEnabled(getActivity())){
                    setOriginCurrentLocation();
                }else{
                    ErrorDialog.getInstance(getActivity()).showErrorDialog("Location not enabled.");
                }
            }
        });
        myLocationDestination = view.findViewById(R.id.myLocationDestination);
        myLocationDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DB.isLocationEnabled(getActivity())){
                    setDestinationCurrentLocation();
                }else{
                    ErrorDialog.getInstance(getActivity()).showErrorDialog("Location not enabled.");
                }
            }
        });


        calculate = view.findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*MoreInfoFragment bottomSheetDialog = new MoreInfoFragment();
                bottomSheetDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
                bottomSheetDialog.show(getActivity().getSupportFragmentManager(), "playlist_info");*/
                if(infoRecieved){
                    String ok = "";
                    int totOk = totOK();
                    switch (totOk){
                        case 0:
                            if(DB.hasCars()){
                                float total = calculateTotalCost();
                                ok = round(total, 2) + "€ x Pers.";
                                PriceDialog.getInstance(getContext()).showInform("Total Cost", ok);
                            }else{
                                ok = "Please setup a car!";
                                DB.makeCustomToast(getActivity(),ok);
                            }
                            break;
                        case 1:
                            ok = "No origin setup";
                            break;
                        case 2:
                            ok = "No destination setup";
                            break;
                        case 3:
                            ok = "Please add number of passengers";
                            break;
                        case 4:
                            ok = "Route has a total of 0km";
                            break;
                        case 5:
                            ok = "Non valid passenger value";
                            break;

                    }
                    if(totOk!=0){
                        DB.makeCustomToast(getActivity(),ok);
                    }
                }else{
                    ErrorDialog.getInstance(getActivity()).showErrorDialog("Waiting for route information");
                }
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

    private void resetValues() {
        totalTollCost = 0.0;
        totalKMCalculats=0;
        totalKM.setText("Total distance: 0 km");
        totalTripCostTXT.setText("Total trip cost: " + round(totalTripCost, 2) + "€");
    }

    private void setDestinationCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                if(origin!=null && (location.getLatitude() == origin.latitude && location.getLongitude() == origin.longitude) ){
                    DB.makeCustomToast(getActivity(),"Origin and destination cannot be the same");
                }else{
                    if(destinationMarker!=null){
                        destinationMarker.remove();
                        destinationMarker = null;
                    }
                    destination = loc;
                    autocompleteDestination.setText("My Location");
                    destinationMarker = map.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_dpin))));
                    updateMapZoom();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void setOriginCurrentLocation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                if(destination!=null && (location.getLatitude() == destination.latitude && location.getLongitude() == destination.longitude) ){
                    DB.makeCustomToast(getActivity(),"Origin and destination cannot be the same");
                }else{
                    if(originMarker!=null){
                        originMarker.remove();
                        originMarker = null;
                    }
                    origin = loc;
                    autocompleteOrigin.setText("My Location");
                    originMarker = map.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_opin))));
                    updateMapZoom();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
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
                infoRecieved = false;
                LoadingDialog.getInstance(getActivity()).showLoadingDialog("Loading");
                RouteManager.getInstance(this).getRouteHeader(origin, destination, DB.getCurrentVehicle().getConsum(), 1.48f, this);
            }
        }

    }

    private float calculateTotalCost() {
        float totalCost;
        float consum = DB.getVehicles().get(ObjectBox.get().boxFor(SavedConfig.class).get(1).selectedVehicle).getConsum();
        float fuelCost = 0;
        float fuelType = DB.getVehicles().get(ObjectBox.get().boxFor(SavedConfig.class).get(1).selectedVehicle).getFuel();
        if(fuelType == 0){
            //diesel
            fuelCost = (float) 1.008;
        }else if(fuelType == 1){
            //gasolina
            fuelCost = (float) 1.48;
        }else if(fuelType == 2){
            //electric
            fuelCost = -1;
        }
        //(7,8*(178/100))*1,48+11,65
        totalCost = (consum*(metersToKM()/100))*fuelCost;
        if(wantsTolls){
            totalCost += totalTollCost;
        }
        return totalCost/Integer.parseInt(totalPassengers.getText().toString());
    }

    private float metersToKM() {
        return totalKMCalculats/1000;
    }

    private int totOK() {
        int ok = 0;
        if(origin==null){
            ok = 1;
        }else if(destination==null){
            ok = 2;
        }else if(totalPassengers.getText().toString().equals("") || totalPassengers.getText().toString().isEmpty()){
            ok = 3;
        }else if(totalKMCalculats == 0){
            ok = 4;
        }else if(Float.parseFloat(totalPassengers.getText().toString())<=0 || Float.parseFloat(totalPassengers.getText().toString())>=10){
            ok = 5;
        }

        return ok;
    }

    private void updateTotalTripCost() {
        totalTripCostTXT.setText("Total trip cost: " + round(totalTripCost, 2) + "€");
    }

    private void makeRecycle(ArrayList<String> tolls){
        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        TollAdapter adapter = new TollAdapter(getActivity(), tolls, this);
        tollRecycle.setLayoutManager(manager2);
        tollRecycle.setAdapter(adapter);
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
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
        if(DB.isLocationEnabled(getActivity())){
            zoomToMyLocation();
        }
        map.resetMinMaxZoomPreference();
        map.setMaxZoomPreference(16.0f);
    }

    private void zoomToMyLocation(){
        if(map!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermission();
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(loc, 3.0f);
                        map.animateCamera(cu);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }


    private void drawRoute() {
        if(origin == destination){
            DB.makeCustomToast(getActivity(),"Origin and destination cannot be the same");
        }else{
            new FetchURL(this).execute(getUrl(origin, destination, "driving"), "driving");
        }
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
        float fuelCost = 0;
        float fuelType = DB.getVehicles().get(ObjectBox.get().boxFor(SavedConfig.class).get(1).selectedVehicle).getFuel();
        if(fuelType == 0){
            //diesel
            fuelCost = (float) 1.008;
        }else if(fuelType == 1){
            //gasolina
            fuelCost = (float) 1.48;
        }else if(fuelType == 2){
            //electric
            fuelCost = -1;
        }
        totalTripCost = (DB.getCurrentVehicle().getConsum()*(metersToKM()/100))*fuelCost;
        updateTotalTripCost();
    }

    @Override
    public void onHeaderRecieved(Summary body) {
        if(body.getTollCost()==null && body.getTotalDist()==-.1){
            DB.makeCustomToast(getActivity(),"No route for these coordinates.");
            map.clear();
            autocompleteOrigin.setText("");
            autocompleteDestination.setText("");
            clearButtonOrigin.setVisibility(View.GONE);
            clearButtonDestination.setVisibility(View.GONE);
        }else{
            if(body.getTollCost().getCar()>0){
                drawRoute();
                updateTotalTripCost();
                totalTollCost = body.getTollCost().getCar()/100;
                DB.makeCustomToast(getActivity(),"This route has tolls");
                tollSwitch.setVisibility(View.VISIBLE);
                totalTollCostTXT.setText("Total toll cost: " + totalTollCost +"€");
                updateTotalTripCost();
                RouteManager.getInstance(this).getRouteRoadsheet(origin, destination, DB.getCurrentVehicle().getConsum(), 1.48f, this);
            }
        }

    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onHeaderFailure(Throwable throwable) {
        ErrorDialog.getInstance(getActivity()).showErrorDialog("App failure. Please restart the app");
    }

    @Override
    public void onFailure(Throwable throwable) {
        ErrorDialog.getInstance(getActivity()).showErrorDialog("App failure. Please restart the app");
    }

    @Override
    public void onRoadSheetRecieved(ArrayList<Object> parse) {
        ArrayList<String> tolls = new ArrayList<>();

        if(parse.get(0).equals("ERROR")){
            DB.makeCustomToast(getActivity(),"No route for these coordinates.");
            map.clear();
            autocompleteOrigin.setText("");
            autocompleteDestination.setText("");
            clearButtonOrigin.setVisibility(View.GONE);
            clearButtonDestination.setVisibility(View.GONE);
        }else{

            for(int i=0; i<parse.size() ;i++){
                String s = ((String)((ArrayList)parse.get(i)).get(7));
                if(s.contains("EUR")){
                    tolls.add(((String)((ArrayList)parse.get(i)).get(7)));
                }
            }
            infoRecieved = true;
            LoadingDialog.getInstance(getActivity()).cancelLoadingDialog();
            makeRecycle(tolls);
        }

    }

    @Override
    public void onRoadSheetFailure(Throwable throwable) {
        ErrorDialog.getInstance(getActivity()).showErrorDialog("App failure. Please restart the app");
    }

    @Override
    public void tollSelected(String item, boolean checked) {
        String[] words = item.split(" ");
        float cost = 0;
        for(int i=0; i<words.length ;i++){
            if(words[i].contains(".") && words[i+1].contains("EUR") ){
                cost = Float.parseFloat(words[i]);
            }
        }
        if(checked){
            totalTollCost+=cost;
        }else{
            totalTollCost-=cost;
        }
        totalTollCostTXT.setText("Total toll cost: " + round(totalTollCost, 2) + "€");
    }

}
