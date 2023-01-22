package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.nmpk.environment.EnvironmentSetup;
import org.nmpk.household.room.light.LightChangeTracker;
import org.nmpk.household.room.light.LightStateActor;

class LightActorSetup {
    static ActorRef setupLightActor(ActorSystem system, String roomId) {
        String lightId = roomId + "-Light1";
        ActorRef lightStateActor = system.actorOf(Props.create(LightStateActor.class, lightId), "LightStateActor-" + lightId);
        ActorRef changeTracker = system.actorOf(Props.create(LightChangeTracker.class, lightId, lightStateActor), "LightChangeTracker-" + lightId);
        EnvironmentSetup.setupLightSensor(system, lightId, changeTracker);
        return lightStateActor;
    }
}
