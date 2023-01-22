package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.nmpk.environment.EnvironmentSetup;
import org.nmpk.household.room.temperature.TemperatureActor;
import org.nmpk.household.room.temperature.TemperatureChangeTracker;

class TemperatureActorSetup {
    static ActorRef setupTemperatureActor(ActorSystem system, String roomId) {
        String temperatureId = roomId + "-Temperature1";
        ActorRef temperatureStateActor = system.actorOf(Props.create(TemperatureActor.class, temperatureId), "TemperatureStateActor-" + temperatureId);
        ActorRef changeTracker = system.actorOf(Props.create(TemperatureChangeTracker.class, temperatureId, temperatureStateActor), "TemperatureChangeTracker-" + temperatureId);
        EnvironmentSetup.setupTemperatureSensor(system, temperatureId, changeTracker);
        return temperatureStateActor;
    }
}
