package org.nmpk.household.room;

import akka.actor.ActorRef;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class BaseRoomActors {
    ActorRef crowdActor;
    ActorRef windowActor;
    ActorRef lightActor;
    ActorRef temperatureActor;
    ActorRef devicesActor;
}
