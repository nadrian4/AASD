package org.nmpk.environment;

import lombok.Value;

@Value
public class WindowSignal {
    String sender;
    long id;
}
