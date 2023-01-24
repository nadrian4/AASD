package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.nmpk.household.room.RoomActors;
import org.nmpk.household.supervisor.HouseholdSupervisor;

import java.util.List;
import java.util.Map;

public class ApartmentSetup {
    public static void setupApartment(ActorSystem system) {
        Map<String, RoomActors> rooms = Map.of(
                "LivingRoom", RoomSetup.setupRoom(system, "LivingRoom", List.of("TV", "Lamp", "AC"))
//                ,"Bedroom-1", RoomSetup.setupRoom(system, "Bedroom-1", List.of("TV", "Lamp", "Humidifier"))
//                ,"Bedroom-2", RoomSetup.setupRoom(system, "Bedroom-2", List.of("Lamp", "Humidifier", "Phone Charger"))
//                ,"Kitchen", RoomSetup.setupRoom(system, "Kitchen", List.of("Boiling pot", "Microwave Oven", "Coffee machine"))
        );
        ActorRef actorRef = system.actorOf(Props.create(HouseholdSupervisor.class, rooms));
    }
}
