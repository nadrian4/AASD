package org.nmpk.environment;

import akka.actor.ActorRef;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
class PeriodicSignalPhotoCellSensor extends PeriodicSignalSensor implements PhotoCellSensor {

    private final String name;
    private final ActorRef signalReceiver;

    PeriodicSignalPhotoCellSensor(ActorRef signalReceiver) {
        super(new RandomEmitIntervalSupplier(50, 150));
        this.name = self().path().name();
        this.signalReceiver = signalReceiver;
    }

    @Override
    void emitSignal() {
        long signalId = ThreadLocalRandom.current().nextLong();
        PhotoCellSignal signal = new PhotoCellSignal(name, signalId);
        signalReceiver.tell(signal, self());
    }
}