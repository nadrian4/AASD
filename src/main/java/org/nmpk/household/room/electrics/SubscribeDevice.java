package org.nmpk.household.room.electrics;

import akka.actor.ActorRef;
import lombok.Value;

@Value
public class SubscribeDevice {
    ActorRef subscriber;
    String deviceId;
}
