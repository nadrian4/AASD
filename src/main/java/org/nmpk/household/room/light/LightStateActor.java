package org.nmpk.household.room.light;

import akka.japi.pf.ReceiveBuilder;
import org.nmpk.household.AbstractHouseholdActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightStateActor extends AbstractHouseholdActor {
    private final Logger log;
    boolean isOpen;
    private final String lightId;

    public LightStateActor(String lightId) {
        this.log = LoggerFactory.getLogger(LightStateActor.class.getName() + "-" + lightId);
        this.lightId = lightId;
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder
                .match(LightOn.class, w -> {
                    this.isOpen = true;
                    log.debug("light on: {}", lightId);
                })
                .match(LightOff.class, w -> {
                    this.isOpen = false;
                    log.debug("light off: {}", lightId);
                })
                ;
    }
}
