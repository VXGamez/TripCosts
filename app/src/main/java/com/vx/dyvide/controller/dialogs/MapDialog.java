package com.vx.dyvide.controller.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.vx.dyvide.R;
import com.vx.dyvide.controller.restAPI.Michelin.managers.RouteManager;
import com.vx.dyvide.model.DB.DB;

public class MapDialog implements OnMapReadyCallback {

    private static MapDialog sManager;
    private Context mContext;
    private Dialog mDialog;
    private Marker o;
    private Marker d;
    private Polyline p;
    private GoogleMap map;
    private int width;
    private int height;


    public static MapDialog getInstance(Context context) {
        if (sManager == null) {
            sManager = new MapDialog(context);
        }
        return sManager;
    }

    public MapDialog(Context context) {
        mContext = context;
        mDialog = null;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.dialog_map);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getScreenResolution(mContext);
    }

    public void cancelDialog(){

        map.clear();
        mDialog.dismiss();
    }

    private void updateMapZoom(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        int padding = 0;
        if(o!=null){
            builder.include(o.getPosition());
            if(padding>0){
                padding=200;
            }else{
                padding=120;
            }
        }
        if(d!=null){
            builder.include(d.getPosition());
            if(padding>0){
                padding=200;
            }else{
                padding=120;
            }
        }

        if(d!=null || d!=null){
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            map.animateCamera(cu);
        }

    }

    private void getScreenResolution(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
    }


    public void showEnlargedMap(Marker originMarker, Marker destinationMarker, Polyline currentPolyline) {
        o = originMarker;
        d = destinationMarker;
        p = currentPolyline;
        LinearLayout layout = mDialog.findViewById(R.id.layoutMap);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height = (int) (height*0.8);
        params.width = (int) (width*0.9);
        layout.setLayoutParams(params);

        FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.mapDialogMapah);
        mapFragment.getMapAsync(this);
        ImageButton minimize = mDialog.findViewById(R.id.makeSmaller);
        minimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog();
            }
        });
        if(map!=null){
            updateMapZoom();
        }

        mDialog.show();
    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = mContext.getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        try {
            boolean success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(mContext, R.raw.map_style));
        } catch (Resources.NotFoundException e) {

        }
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.resetMinMaxZoomPreference();
        map.setMaxZoomPreference(16.0f);
        if(o!=null){
            map.addMarker(new MarkerOptions().position(o.getPosition()).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_opin))));
        }
        if(d!=null){
            map.addMarker(new MarkerOptions().position(d.getPosition()).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_dpin))));
        }
        if(p!=null){
            PolylineOptions poly = new PolylineOptions().width(20).color(Color.parseColor("#58A600")).geodesic(true);
            poly.addAll(p.getPoints());
            map.addPolyline(poly);
        }
        updateMapZoom();
    }
}
