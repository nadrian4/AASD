package org.nmpk.household.room.temperature;

public interface TemperatureController {
    void raise(double delta);
    void lower(double delta);
}
