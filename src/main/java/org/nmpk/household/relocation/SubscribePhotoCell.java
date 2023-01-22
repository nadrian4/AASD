package org.nmpk.household.relocation;

import akka.actor.ActorRef;
import lombok.Value;

@Value
class SubscribePhotoCell {
    String roomId;
    ActorRef updatesReceiver;
}
