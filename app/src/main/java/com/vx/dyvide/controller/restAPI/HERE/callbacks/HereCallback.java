package com.vx.dyvide.controller.restAPI.HERE.callbacks;

import com.vx.dyvide.model.PriceResponse;

public interface HereCallback {
    void pricesRecieved(PriceResponse body);
    void onFailure(Throwable throwable);
    void pricesFailure(Throwable throwable);
}
