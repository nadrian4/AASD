package org.nmpk.household.relocation;

import akka.actor.ActorRef;
import lombok.Value;

@Value
public class SubscribeRelocation {
    ActorRef subscriber;
}
