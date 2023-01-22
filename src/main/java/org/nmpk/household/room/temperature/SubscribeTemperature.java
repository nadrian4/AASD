package org.nmpk.household.room.temperature;

import akka.actor.ActorRef;
import lombok.Value;

@Value
public class SubscribeTemperature {
    ActorRef subscriber;
}
