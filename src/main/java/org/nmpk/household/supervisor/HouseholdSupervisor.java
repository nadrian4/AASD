package org.nmpk.household.supervisor;

import akka.japi.pf.ReceiveBuilder;
import org.nmpk.household.AbstractHouseholdActor;
import org.nmpk.household.room.ControlRoomActors;
import org.nmpk.household.room.command.LightCommandType;
import org.nmpk.household.room.command.LightTurnCommand;

import java.util.Map;

public class HouseholdSupervisor extends AbstractHouseholdActor {
    private final Map<String, ControlRoomActors> roomControllers;

    HouseholdSupervisor(Map<String, ControlRoomActors> roomControllers) {
        this.roomControllers = roomControllers;
        for (ControlRoomActors value : roomControllers.values()) {
            value.getRoomController()
                    .tell(new LightTurnCommand(LightCommandType.on), self());
        }
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder;
    }
}
