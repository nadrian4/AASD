package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.nmpk.household.room.BaseRoomActors;
import org.nmpk.household.room.StateAggregatorActor;

public class StateAggregatorSetup {
    public static ActorRef setupStateAggregatorActor(ActorSystem system, BaseRoomActors roomActors, String roomId) {
        return system.actorOf(Props.create(StateAggregatorActor.class, roomActors, roomId), roomId + "-StateAggregator");
    }
}
