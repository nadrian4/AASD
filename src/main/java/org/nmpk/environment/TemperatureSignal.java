package org.nmpk.environment;

import lombok.Value;

@Value
public class TemperatureSignal {
    String sender;
    long value;
}
