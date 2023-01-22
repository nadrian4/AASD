package org.nmpk.household.room.crowd;

import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import akka.protobuf.LazyStringArrayList;
import org.nmpk.household.AbstractHouseholdActor;
import org.nmpk.household.relocation.EnteredRoom;
import org.nmpk.household.relocation.LeftRoom;
import org.nmpk.household.relocation.SubscribeRelocation;
import org.nmpk.household.room.temperature.SubscribeTemperature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class CrowdActor extends AbstractHouseholdActor {
    private final Logger log;
    private long peopleInRoom;
    private final Set<ActorRef> subscribers;

    CrowdActor(long initialCount, String roomId, ActorRef roomEnterActor) {
        this.log = LoggerFactory.getLogger(CrowdActor.class.getName() + "-" + roomId);
        this.peopleInRoom = initialCount;
        roomEnterActor.tell(new SubscribeRelocation(self()), self());
        this.subscribers = new HashSet<>();
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder
                .match(EnteredRoom.class, e -> {
                    peopleInRoom++;
                    log.debug("Someone entered. People in room: {}", peopleInRoom);
                    broadcastNewCrowdState();
                })
                .match(LeftRoom.class, e -> {
                    peopleInRoom = Math.max(0, peopleInRoom - 1);
                    log.debug("Someone left. People in room: {}", peopleInRoom);
                    broadcastNewCrowdState();
                })
                .match(SubscribeCrowd.class, subscribeCrowd -> {
                    log.debug("New subscriber: {}", subscribeCrowd);
                    this.subscribers.add(subscribeCrowd.getSubscriber());
                })
                ;
    }

    private void broadcastNewCrowdState() {
        for (ActorRef subscriber : subscribers) {
            subscriber.tell(new CrowdState(peopleInRoom), self());
        }
    }
}
