package org.nmpk.household.room.command;

import lombok.Value;

@Value
public class DeviceCommand {
    DeviceCommandType commandType;
    String deviceId;
}
