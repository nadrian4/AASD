package org.nmpk.household.room.light;

import akka.actor.ActorRef;
import lombok.Value;

@Value
public class SubscribeLight {
    ActorRef subscriber;
}
