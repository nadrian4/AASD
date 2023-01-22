package org.nmpk.environment;

public interface TemperatureController {
    void raise(double delta);
    void lower(double delta);
}
