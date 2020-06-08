package com.vx.dyvide.controller.restAPI.Michelin.callbacks;

import com.vx.dyvide.model.HERE.PriceResponse;
import com.vx.dyvide.model.Michelin.Iti;
import com.vx.dyvide.model.Michelin.ItiRoadsheet;
import com.vx.dyvide.model.Michelin.RoadSheet;
import com.vx.dyvide.model.Michelin.Summary;

import java.util.ArrayList;

import retrofit2.Response;

public interface MichelinCallback {
    
    void onHeaderRecieved(Summary body);

    void onHeaderFailure(Throwable throwable);

    void onFailure(Throwable throwable);

    void onRoadSheetRecieved(ArrayList<Object> parseJsonRoadsheet);

    void onRoadSheetFailure(Throwable throwable);
}
