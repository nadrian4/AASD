package org.nmpk.household.room.temperature;

import lombok.*;

import java.text.DecimalFormat;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
class NewTemperatureRead {
    private static final DecimalFormat temperatureFormat = new DecimalFormat("0.00");
    final double temperature;
    final TemperatureUnit unit = TemperatureUnit.CELSIUS;

    static NewTemperatureRead empty() {
        return new NewTemperatureRead(0d);
    }

    @Override
    public String toString() {
        return temperatureFormat.format(temperature) + unit.toString();
    }
}
