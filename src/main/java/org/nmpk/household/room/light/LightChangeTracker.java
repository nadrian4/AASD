package org.nmpk.household.room.light;

import akka.actor.ActorRef;
import org.nmpk.environment.LightSignal;
import org.nmpk.household.AbstractHouseholdActorWithFSM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightChangeTracker extends AbstractHouseholdActorWithFSM<LightState, Integer> {
    private final Logger log;
    private final ActorRef lightStateActor;

    LightChangeTracker(String lightId, ActorRef lightStateActor) {
        this.log = LoggerFactory.getLogger(LightChangeTracker.class.getName() + "-" + lightId);
        this.lightStateActor = lightStateActor;
        initializeStateMachine();
    }

    private void initializeStateMachine() {
        startWith(LightState.off, 0);
        when(LightState.off, matchEvent(
                LightSignal.class, (c, d) -> {
                    if (c.getId() % 500 == 0) {
                        lightStateActor.tell(new LightOn(), self());
                        return goTo(LightState.on);
                    }
                    return stay();
                }
        ));
        when(LightState.on, matchEvent(
                LightSignal.class, (c, d) -> {
                    if (c.getId() % 500 == 0) {
                        lightStateActor.tell(new LightOff(), self());
                        return goTo(LightState.off);
                    }
                    return stay();
                }
        ));
        initialize();
    }
}
