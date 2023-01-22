package org.nmpk.household.room;

import akka.japi.pf.ReceiveBuilder;
import org.nmpk.household.AbstractHouseholdActor;


// todo: Czy ten aggregator jest potrzebny w ogóle?
public class StateAggregatorActor extends AbstractHouseholdActor {

    private final String roomId;
    private final BaseRoomActors roomActors;

    StateAggregatorActor(BaseRoomActors roomActors, String roomId) {
        this.roomId = roomId;
        this.roomActors = roomActors;
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder;
    }
}
