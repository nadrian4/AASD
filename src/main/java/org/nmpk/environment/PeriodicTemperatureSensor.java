package org.nmpk.environment;

import akka.actor.ActorRef;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
class PeriodicTemperatureSensor extends PeriodicSignalSensor implements PhotoCellSensor {

    private final String name;
    private final ActorRef signalReceiver;

    PeriodicTemperatureSensor(ActorRef signalReceiver) {
        super(new RandomEmitIntervalSupplier(200, 1000));
        this.name = self().path().name();
        this.signalReceiver = signalReceiver;
    }

    @Override
    void emitSignal() {
        long signalId = ThreadLocalRandom.current().nextLong();
        TemperatureSignal signal = new TemperatureSignal(name, signalId);
        signalReceiver.tell(signal, self());
    }
}