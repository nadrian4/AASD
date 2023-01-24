package org.nmpk.household.room.electrics;

import lombok.Value;

import java.time.Duration;

@Value
public class PowerConsumptionRead {
    public static final Duration DEFAULT_PERIOD = Duration.ofSeconds(1);
    double consumption;
    String deviceId;
    PowerUnit unit = PowerUnit.KWH;
    Duration period = DEFAULT_PERIOD;

    static PowerConsumptionRead empty(String deviceId) {
        return new PowerConsumptionRead(0, deviceId);
    }
}
