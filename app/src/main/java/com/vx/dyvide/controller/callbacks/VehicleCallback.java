package com.vx.dyvide.controller.callbacks;

import com.vx.dyvide.model.Vehicle;

public interface VehicleCallback {
    void createNewVehicle();
    void vehiclePressed(Vehicle vehicle, int position);
}
