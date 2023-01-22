package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.nmpk.environment.BuiltInTemperatureController;
import org.nmpk.environment.EnvironmentSetup;
import org.nmpk.household.room.temperature.TemperatureActor;
import org.nmpk.household.room.temperature.TemperatureChangeTracker;

import java.util.Optional;

class TemperatureActorSetup {
    static ActorRef setupTemperatureActor(ActorSystem system, String roomId) {
        String temperatureId = roomId + "-Temperature1";
        BuiltInTemperatureController temperatureController = new BuiltInTemperatureController();
        ActorRef temperatureStateActor = system.actorOf(Props.create(TemperatureActor.class, temperatureId, temperatureController), "TemperatureStateActor-" + temperatureId);
        ActorRef changeTracker = system.actorOf(Props.create(TemperatureChangeTracker.class, temperatureId, temperatureStateActor), "TemperatureChangeTracker-" + temperatureId);
        temperatureController.setTemperatureTracker(Optional.of(changeTracker));
        EnvironmentSetup.setupTemperatureSensor(system, temperatureId, changeTracker);
        return temperatureStateActor;
    }
}
