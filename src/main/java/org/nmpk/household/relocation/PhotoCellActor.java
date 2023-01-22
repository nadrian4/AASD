package org.nmpk.household.relocation;

import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import org.nmpk.environment.PhotoCellSignal;
import org.nmpk.household.AbstractHouseholdActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class PhotoCellActor extends AbstractHouseholdActor {
    private final Logger log;
    private final Set<ActorRef> subscribers;
    private final String id;
    private int counter = 0;

    PhotoCellActor(String id) {
        this.log= LoggerFactory.getLogger(PhotoCellActor.class.getName() + "-" + id);
        this.id = id;
        this.subscribers = new HashSet<>();
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder
                .match(SubscribePhotoCell.class, this::onSubscribeRequest)
                .match(PhotoCellSignal.class, signal -> {
                    counter++;
                    PhotoCellState newState = counter % 2 == 0 ? PhotoCellState.OFF : PhotoCellState.ON;
                    subscribers.forEach(subscriber -> subscriber.tell(new NewPhotoCellState(id, newState), self()));
                });
    }

    private void onSubscribeRequest(SubscribePhotoCell s) {
        log.info("New subscriber: {}", s);
        subscribers.add(s.getUpdatesReceiver());
    }
}
