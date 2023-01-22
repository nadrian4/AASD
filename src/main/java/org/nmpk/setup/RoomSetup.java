package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

class RoomSetup {
    public static void setupRoom(ActorSystem system, String roomId) {
        ActorRef crowdActor = CrowdActorSetup.setupCrowdActor(system, roomId);
        ActorRef windowActor = WindowActorSetup.setupWindowActor(system, roomId);
    }
}
