package org.nmpk.household.room.crowd;

import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import org.nmpk.household.AbstractHouseholdActor;
import org.nmpk.household.relocation.EnteredRoom;
import org.nmpk.household.relocation.LeftRoom;
import org.nmpk.household.relocation.SubscribeRelocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrowdActor extends AbstractHouseholdActor {
    private final Logger log;
    private long peopleInRoom;

    CrowdActor(long initialCount, String roomId, ActorRef roomEnterActor) {
        this.log = LoggerFactory.getLogger(CrowdActor.class.getName() + "-" + roomId);
        this.peopleInRoom = initialCount;
        roomEnterActor.tell(new SubscribeRelocation(self()), self());
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder
                .match(EnteredRoom.class, e -> {
                    peopleInRoom++;
                    log.debug("Someone entered. People in room: {}", peopleInRoom);
                })
                .match(LeftRoom.class, e -> {
                    peopleInRoom = Math.max(0, peopleInRoom - 1);
                    log.debug("Someone left. People in room: {}", peopleInRoom);
                });
    }
}
