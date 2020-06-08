package com.vx.dyvide.controller.restAPI.Michelin.callbacks;

import com.vx.dyvide.model.HERE.PriceResponse;
import com.vx.dyvide.model.Michelin.Iti;
import com.vx.dyvide.model.Michelin.Resposta;

import retrofit2.Response;

public interface MichelinCallback {
    
    void onHeaderRecieved(Resposta body);

    void onHeaderFailure(Throwable throwable);

    void onFailure(Throwable throwable);
}
