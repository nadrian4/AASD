package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.nmpk.household.room.BaseRoomActors;
import org.nmpk.household.room.controller.RoomController;

public class RoomControllerSetup {
    public static ActorRef setupRoomController(ActorSystem system, BaseRoomActors roomActors, String roomId) {
        return system.actorOf(Props.create(RoomController.class, roomActors, roomId), roomId + "-RoomController");
    }
}
