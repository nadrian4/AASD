package org.nmpk.household.supervisor;

import akka.japi.pf.ReceiveBuilder;
import org.nmpk.household.AbstractHouseholdActor;
import org.nmpk.household.room.RoomActors;
import org.nmpk.household.room.command.LightCommandType;
import org.nmpk.household.room.command.LightTurnCommand;

import java.util.Map;

public class HouseholdSupervisor extends AbstractHouseholdActor {
    private final Map<String, RoomActors> roomControllers;

    HouseholdSupervisor(Map<String, RoomActors> roomControllers) {
        this.roomControllers = roomControllers;
        for (RoomActors value : roomControllers.values()) {
            value.getRoomController()
                    .tell(new LightTurnCommand(LightCommandType.on), self());
        }
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder;
    }
}
