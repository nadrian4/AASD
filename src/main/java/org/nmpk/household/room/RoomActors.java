package org.nmpk.household.room;

import akka.actor.ActorRef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class RoomActors {
    ActorRef crowdActor;
    ActorRef windowActor;
    ActorRef lightActor;
    ActorRef temperatureActor;
}
