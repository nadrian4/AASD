package org.nmpk.setup;

import akka.actor.ActorSystem;

public class ApartmentSetup {
    public static void setupApartment(ActorSystem system) {
        RoomSetup.setupRoom(system, "LivingRoom");
        RoomSetup.setupRoom(system, "Bedroom");
        RoomSetup.setupRoom(system, "Kitchen");
    }
}
