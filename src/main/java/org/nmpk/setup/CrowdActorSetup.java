package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.nmpk.household.room.crowd.CrowdActor;

import java.util.concurrent.ThreadLocalRandom;

class CrowdActorSetup {
    static ActorRef setupCrowdActor(ActorSystem system, String roomId) {
        ActorRef roomEnterActor = RoomEnterSetup.setupRoomEnter(system, roomId);
        long initialPplCount = ThreadLocalRandom.current().nextLong() % 8 + 2;
        return system.actorOf(Props.create(CrowdActor.class, initialPplCount, roomId, roomEnterActor), "CrowdActor-" + roomId);
    }
}
