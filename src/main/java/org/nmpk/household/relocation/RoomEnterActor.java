package org.nmpk.household.relocation;

import akka.actor.AbstractFSM;
import akka.actor.ActorRef;
import org.nmpk.household.AbstractHouseholdActorWithFSM;
import org.nmpk.setup.PhotoCellSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class RoomEnterActor extends AbstractHouseholdActorWithFSM<RelocationState, Integer> {
    private final Logger log;
    private final String roomId;
    private final Set<ActorRef> subscribers;

    RoomEnterActor(String roomId) {
        this.log = LoggerFactory.getLogger(RoomEnterActor.class.getName() + "-" + roomId);
        this.roomId = roomId;
        this.subscribers = new HashSet<>();
        String cell1Id = roomId + "-PC-1";
        String cell2Id = roomId + "-PC-2";
        ActorRef cell1 = PhotoCellSetup.setupCell(context().system(), cell1Id);
        ActorRef cell2 = PhotoCellSetup.setupCell(context().system(), cell2Id);
        subscribePhotoCells(Set.of(cell1, cell2));
        initializeRoomActor(roomId, cell1Id, cell2Id);
    }

    void subscribePhotoCells(Set<ActorRef> cells) {
        cells.forEach(cell -> cell.tell(new SubscribePhotoCell(roomId, self()), self()));
    }

    private void initializeRoomActor(String roomId, String cell1Id, String cell2Id) {
        startWith(RelocationState.idle, 0);
        whenUnhandled(matchEvent(
                SubscribeRelocation.class, (c, d) -> {
                    subscribers.add(c.getSubscriber());
                    return stay();
                }
        ));
        when(RelocationState.idle,
                matchEvent(NewPhotoCellState.class, (c, d) -> {
                    if (c.refersTo(cell1Id) && c.isOn()) {
                        return goTo(RelocationState.startedEntering);
                    } else if (c.refersTo(cell2Id) && c.isOn()) {
                        return goTo(RelocationState.startedLeaving);
                    }
                    return stay();
                }));
        when(RelocationState.startedEntering,
                matchEvent(NewPhotoCellState.class, (c, d) -> {
                    if (c.refersTo(cell1Id) && c.isOff()) {
                        return goTo(RelocationState.idle);
                    } else if (c.refersTo(cell2Id) && c.isOn()) {
                        return goTo(RelocationState.proceededEntering);
                    }
                    return stay();
                }));
        when(RelocationState.proceededEntering,
                matchEvent(NewPhotoCellState.class, (c, d) -> {
                    if (c.refersTo(cell1Id) && c.isOff()) {
                        return goTo(RelocationState.almostEntered);
                    } else if (c.refersTo(cell2Id) && c.isOff()) {
                        return goTo(RelocationState.startedEntering);
                    }
                    return stay();
                }));
        when(RelocationState.almostEntered,
                matchEvent(NewPhotoCellState.class, (c, d) -> {
                    if (c.refersTo(cell1Id) && c.isOn()) {
                        return goTo(RelocationState.proceededEntering);
                    } else if (c.refersTo(cell2Id) && c.isOff()) {
                        onRoomEntered(roomId);
                        return goTo(RelocationState.idle);
                    }
                    return stay();
                }));
        when(RelocationState.startedLeaving,
                matchEvent(NewPhotoCellState.class, (c, d) -> {
                    if (c.refersTo(cell1Id) && c.isOn()) {
                        return goTo(RelocationState.proceededLeaving);
                    } else if (c.refersTo(cell2Id) && c.isOff()) {
                        return goTo(RelocationState.idle);
                    }
                    return stay();

                }));
        when(RelocationState.proceededLeaving,
                matchEvent(NewPhotoCellState.class, (c, d) -> {
                    if (c.refersTo(cell1Id) && c.isOff()) {
                        return goTo(RelocationState.startedLeaving);
                    } else if (c.refersTo(cell2Id) && c.isOff()) {
                        return goTo(RelocationState.almostLeft);
                    }
                    return stay();

                }));
        when(RelocationState.almostLeft,
                matchEvent(NewPhotoCellState.class, (c, d) -> {
                    if (c.refersTo(cell1Id) && c.isOff()) {
                        onRoomLeft(roomId);
                        return goTo(RelocationState.idle);
                    } else if (c.refersTo(cell2Id) && c.isOn()) {
                        return goTo(RelocationState.proceededLeaving);
                    }
                    return stay();
                }));
        initialize();
    }

    private void onRoomEntered(String roomId) {
        subscribers.forEach(s -> s.tell(new EnteredRoom(), self()));
    }

    private void onRoomLeft(String roomId) {
        subscribers.forEach(s -> s.tell(new LeftRoom(), self()));
    }
}
