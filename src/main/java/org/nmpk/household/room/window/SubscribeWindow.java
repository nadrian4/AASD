package org.nmpk.household.room.window;

import akka.actor.ActorRef;
import lombok.Value;

@Value
public class SubscribeWindow {
    ActorRef subscriber;
}
