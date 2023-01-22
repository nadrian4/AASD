package org.nmpk.setup;

import akka.actor.ActorSystem;
import org.nmpk.household.room.RoomActors;

public class ApartmentSetup {
    public static void setupApartment(ActorSystem system) {
        RoomActors livingRoomActors = RoomSetup.setupRoom(system, "LivingRoom");
        RoomActors bedroomActors = RoomSetup.setupRoom(system, "Bedroom");
        RoomActors kitchenActors = RoomSetup.setupRoom(system, "Kitchen");
    }
}
