package org.nmpk.environment;

import akka.actor.ActorRef;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
class PeriodicWindowSensor extends PeriodicSignalSensor implements PhotoCellSensor {
    private final String name;
    private final ActorRef signalReceiver;

    PeriodicWindowSensor(ActorRef signalReceiver) {
        super(new RandomEmitIntervalSupplier(10, 50));
        this.name = self().path().name();
        this.signalReceiver = signalReceiver;
    }

    @Override
    void emitSignal() {
        long signalId = ThreadLocalRandom.current().nextLong();
        WindowSignal signal = new WindowSignal(name, signalId);
        signalReceiver.tell(signal, self());
    }
}