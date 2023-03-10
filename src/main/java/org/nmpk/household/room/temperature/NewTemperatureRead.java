package org.nmpk.household.room.temperature;

import lombok.*;

import java.text.DecimalFormat;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class NewTemperatureRead {
    private static final DecimalFormat temperatureFormat = new DecimalFormat("0.00");
    final double temperature;
    final TemperatureUnit unit = TemperatureUnit.CELSIUS;

    public static NewTemperatureRead empty() {
        return new NewTemperatureRead(0d);
    }

    @Override
    public String toString() {
        return temperatureFormat.format(temperature) + unit.toString();
    }

    public boolean isHigh() {
        return temperature > 21d;
    }

    public boolean isLow() {
        return temperature < 17.6d;
    }
}
