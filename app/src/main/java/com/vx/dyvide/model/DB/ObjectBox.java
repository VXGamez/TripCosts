package com.vx.dyvide.model.DB;


import android.content.Context;
import android.util.Log;

import io.objectbox.android.BuildConfig;

import io.objectbox.BoxStore;

public class ObjectBox {
    private static BoxStore boxStore;

    public static void init(Context context) {
        if(boxStore==null)
            boxStore = MyObjectBox.builder().androidContext(context.getApplicationContext()).build();

    }

    public static BoxStore get() {
        return boxStore;
    }
}
