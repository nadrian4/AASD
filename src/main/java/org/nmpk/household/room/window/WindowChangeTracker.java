package org.nmpk.household.room.window;

import akka.actor.ActorRef;
import org.nmpk.environment.WindowSignal;
import org.nmpk.household.AbstractHouseholdActorWithFSM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindowChangeTracker extends AbstractHouseholdActorWithFSM<WindowState, Integer> {
    private final Logger log;
    private final ActorRef windowStateActor;

    WindowChangeTracker(String windowId, ActorRef windowStateActor) {
        this.log = LoggerFactory.getLogger(WindowChangeTracker.class.getName() + "-" + windowId);
        this.windowStateActor = windowStateActor;
        initializeStateMachine();
    }

    private void initializeStateMachine() {
        startWith(WindowState.closed, 0);
        when(WindowState.closed, matchEvent(
                WindowSignal.class, (c, d) -> {
                    if (c.getId() % 500 == 0) {
                        windowStateActor.tell(new WindowOpened(), self());
                        return goTo(WindowState.opened);
                    }
                    return stay();
                }
        ));
        when(WindowState.opened, matchEvent(
                WindowSignal.class, (c, d) -> {
                    if (c.getId() % 500 == 0) {
                        windowStateActor.tell(new WindowClosed(), self());
                        return goTo(WindowState.closed);
                    }
                    return stay();
                }
        ));
    }
}
