package org.nmpk.environment;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import lombok.extern.slf4j.Slf4j;
import org.nmpk.timer.PeriodicActor;

import java.util.function.Supplier;

@Slf4j
abstract class PeriodicSignalSensor extends AbstractEnvironmentActor {
    private final ActorRef periodicActor;

    PeriodicSignalSensor(long emitSignalIntervalInMs) {
        this(() -> emitSignalIntervalInMs);
    }

    PeriodicSignalSensor(Supplier<Long> intervalSupplier) {
        this.periodicActor = context().system().actorOf(
                Props.create(PeriodicActor.class, intervalSupplier, self()),
                "PhotoCellSensor-Timer-" + self().path().name());
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder
                .match(PeriodicActor.Tick.class, tick -> {
                    emitSignal();
                });
    }
}
