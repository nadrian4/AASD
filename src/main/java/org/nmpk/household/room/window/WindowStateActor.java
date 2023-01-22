package org.nmpk.household.room.window;

import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import akka.protobuf.LazyStringArrayList;
import org.nmpk.household.AbstractHouseholdActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class WindowStateActor extends AbstractHouseholdActor {
    private final Logger log;
    boolean isOpen;
    private final String windowId;
    private final Set<ActorRef> subscribers;

    public WindowStateActor(String windowId) {
        this.log = LoggerFactory.getLogger(WindowStateActor.class.getName() + "-" + windowId);
        this.windowId = windowId;
        this.subscribers = new HashSet<>();
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder
                .match(WindowOpened.class, w -> {
                    this.isOpen = true;
                    log.debug("Window opened: {}", windowId);
                    broadcastNewWindowState();
                })
                .match(WindowClosed.class, w -> {
                    this.isOpen = false;
                    log.debug("Window closed: {}", windowId);
                    broadcastNewWindowState();
                })
                .match(SubscribeWindow.class, subscribeWindow -> {
                    log.debug("New subscriber: {}", subscribeWindow);
                    this.subscribers.add(subscribeWindow.getSubscriber());
                })
                ;
    }

    private void broadcastNewWindowState() {
        for (ActorRef subscriber : subscribers) {
            subscriber.tell(new NewWindowState(isOpen), self());
        }
    }
}
