package org.nmpk.environment;

import lombok.Value;

@Value
public class DeviceSignal {
    String sender;
    long value;
}
