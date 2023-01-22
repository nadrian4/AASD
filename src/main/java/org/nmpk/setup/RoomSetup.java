package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.nmpk.household.room.RoomActors;

class RoomSetup {
    public static RoomActors setupRoom(ActorSystem system, String roomId) {
        ActorRef crowdActor = CrowdActorSetup.setupCrowdActor(system, roomId);
        ActorRef windowActor = WindowActorSetup.setupWindowActor(system, roomId);
        ActorRef lightActor = LightActorSetup.setupLightActor(system, roomId);
        ActorRef temperatureActor = TemperatureActorSetup.setupTemperatureActor(system, roomId);
        return RoomActors.builder()
                .crowdActor(crowdActor)
                .windowActor(windowActor)
                .lightActor(lightActor)
                .temperatureActor(temperatureActor)
                .build();
    }
}
