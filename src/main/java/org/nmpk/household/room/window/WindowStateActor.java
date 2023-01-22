package org.nmpk.household.room.window;

import akka.japi.pf.ReceiveBuilder;
import org.nmpk.household.AbstractHouseholdActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindowStateActor extends AbstractHouseholdActor {
    private final Logger log;
    boolean isOpen;
    private final String windowId;

    public WindowStateActor(String windowId) {
        this.log = LoggerFactory.getLogger(WindowStateActor.class.getName() + "-" + windowId);
        this.windowId = windowId;
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder
                .match(WindowOpened.class, w -> {
                    this.isOpen = true;
                    log.info("Window opened: {}", windowId);
                })
                .match(WindowClosed.class, w -> {
                    this.isOpen = false;
                    log.info("Window closed: {}", windowId);
                })
                ;
    }
}
