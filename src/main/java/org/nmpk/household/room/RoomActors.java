package org.nmpk.household.room;

import akka.actor.ActorRef;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class RoomActors {
    ActorRef stateAggregatorActor;
    ActorRef roomController;
}
