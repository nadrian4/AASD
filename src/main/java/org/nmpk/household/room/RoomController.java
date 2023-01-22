package org.nmpk.household.room;

import akka.japi.pf.ReceiveBuilder;
import org.nmpk.household.AbstractHouseholdActor;
import org.nmpk.household.room.command.LightTurnCommand;
import org.nmpk.household.room.command.TemperatureChangeCommand;
import org.nmpk.household.room.command.WindowCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoomController extends AbstractHouseholdActor {
    private final Logger log;
    private final BaseRoomActors roomActors;

    RoomController(BaseRoomActors roomActors, String roomId) {
        this.log = LoggerFactory.getLogger(RoomController.class.getName() + "-" + roomId);
        this.roomActors = roomActors;
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder
                .match(LightTurnCommand.class, this::onLightTurnCommand)
                .match(TemperatureChangeCommand.class, this::onTemperatureChangeCommand)
                .match(WindowCommand.class, this::onWindowCommand)
                ;
    }

    private void onLightTurnCommand(LightTurnCommand command) {
        log.info("New light turn command: {}", command);
    }

    private void onTemperatureChangeCommand(TemperatureChangeCommand command) {
        log.info("New temperature change command: {}", command);
    }

    private void onWindowCommand(WindowCommand command) {
        log.info("New window command: {}", command);
    }

}
