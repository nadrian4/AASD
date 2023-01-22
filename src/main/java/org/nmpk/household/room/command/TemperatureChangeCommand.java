package org.nmpk.household.room.command;

import lombok.Value;

@Value
public class TemperatureChangeCommand {
    TemperatureCommandType commandType;
    double delta;
}
