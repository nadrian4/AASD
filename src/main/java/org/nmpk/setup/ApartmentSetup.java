package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.nmpk.household.room.RoomActors;
import org.nmpk.household.supervisor.HouseholdSupervisor;

import java.util.Map;

public class ApartmentSetup {
    public static void setupApartment(ActorSystem system) {
        Map<String, RoomActors> rooms = Map.of(
                "LivingRoom", RoomSetup.setupRoom(system, "LivingRoom"),
                "Bedroom-1", RoomSetup.setupRoom(system, "Bedroom-1"),
                "Bedroom-2", RoomSetup.setupRoom(system, "Bedroom-2"),
                "Kitchen", RoomSetup.setupRoom(system, "Kitchen")
        );
        ActorRef actorRef = system.actorOf(Props.create(HouseholdSupervisor.class, rooms));
    }
}
