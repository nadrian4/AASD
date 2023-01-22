package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.nmpk.household.room.ControlRoomActors;
import org.nmpk.household.supervisor.HouseholdSupervisor;

import java.util.Map;

public class ApartmentSetup {
    public static void setupApartment(ActorSystem system) {
        Map<String, ControlRoomActors> rooms = Map.of(
                "LivingRoom", RoomSetup.setupRoom(system, "LivingRoom"),
                "Bedroom", RoomSetup.setupRoom(system, "Bedroom"),
                "Kitchen", RoomSetup.setupRoom(system, "Kitchen")
        );
        ActorRef actorRef = system.actorOf(Props.create(HouseholdSupervisor.class, rooms));
    }
}
