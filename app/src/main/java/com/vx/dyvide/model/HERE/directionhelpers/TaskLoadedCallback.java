package com.vx.dyvide.model.HERE.directionhelpers;

/**
 * Created by Vishal on 10/20/2018.
 */

public interface TaskLoadedCallback {
    void onTaskDone(Object... values);
    void totalValues(String totalKM, int totalDistanceValue, String totalDuration, int totalDurationValue);
}
