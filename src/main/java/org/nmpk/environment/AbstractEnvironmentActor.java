package org.nmpk.environment;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class AbstractEnvironmentActor extends AbstractActor {
    @Override
    public AbstractActor.Receive createReceive() {
        ReceiveBuilder builder = ReceiveBuilder.create();
        return addToReceive(builder)
                .matchAny(o -> {
                    log.info("Unknown message: {}", o);
                }).build();
    }

    abstract ReceiveBuilder addToReceive(ReceiveBuilder builder);

    abstract void emitSignal();
}
