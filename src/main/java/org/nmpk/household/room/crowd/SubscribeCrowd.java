package org.nmpk.household.room.crowd;

import akka.actor.ActorRef;
import lombok.Value;

@Value
public class SubscribeCrowd {
    ActorRef subscriber;
}
