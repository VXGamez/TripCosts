package com.vx.dyvide.controller.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vx.dyvide.R;
import com.vx.dyvide.model.DB.DB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("SameParameterValue")
public class MoreInfoFragment extends BottomSheetDialogFragment  {

    private PieChart chart;
    private final String[] parties = new String[] {"Fuel", "Toll"};
    private Double totalTripCost;
    private Double totalTolls;
    private Double totalFuel;


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        //((PlaylistActivity)getActivity()).onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        DB.setLanguage(getActivity());

        View view = inflater.inflate(R.layout.moreinfo_fragment, container, false);

        chart = view.findViewById(R.id.pieChart);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setDrawHoleEnabled(false);
        chart.setTransparentCircleColor(Color.TRANSPARENT);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(true);
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(true);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setTextColor(Color.WHITE);
        l.setTextSize(15);
        chart.setMaxAngle(180f);
        chart.setRotationAngle(180f);
        chart.setCenterTextOffset(0, -20);
        setData(2, 100);
        chart.animateY(1400, Easing.EaseInOutQuad);

        return view;
    }


    private void setData(int count, float range) {

        ArrayList<PieEntry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            values.add(new PieEntry((float) ((Math.random() * range) + range / 5), parties[i % parties.length]));
        }

        PieDataSet dataSet = new PieDataSet(values, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        chart.invalidate();
    }


}
