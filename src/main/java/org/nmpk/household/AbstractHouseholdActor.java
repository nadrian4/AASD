package org.nmpk.household;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractHouseholdActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        ReceiveBuilder builder = ReceiveBuilder.create();
        return addToReceive(builder)
                .matchAny(o -> {
                    log.info("Unknown message: {}. I am: {}", o, self().path());
                })
                .build();
    }

    protected abstract ReceiveBuilder addToReceive(ReceiveBuilder builder);
}
