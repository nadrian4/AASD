package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.nmpk.household.relocation.RoomEnterActor;

public class RoomEnterSetup {
    public static ActorRef setupRoomEnter(ActorSystem system, String roomId) {
        return system.actorOf(
                Props.create(RoomEnterActor.class, roomId), "RoomEnter-" + roomId);
    }
}
