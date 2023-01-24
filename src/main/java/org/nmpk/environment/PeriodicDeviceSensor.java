package org.nmpk.environment;

import akka.actor.ActorRef;
import lombok.extern.slf4j.Slf4j;
import scala.concurrent.duration.Duration;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
class PeriodicDeviceSensor extends PeriodicSignalSensor implements PhotoCellSensor {
    private static final Duration emitSignalPeriod = Duration.create(1, TimeUnit.SECONDS);
    private final String name;
    private final ActorRef signalReceiver;

    PeriodicDeviceSensor(ActorRef signalReceiver) {
        super(new RandomEmitIntervalSupplier(emitSignalPeriod.toMillis() - 1, emitSignalPeriod.toMillis() + 1));
        this.name = self().path().name();
        this.signalReceiver = signalReceiver;
    }

    @Override
    void emitSignal() {
        long signalId = ThreadLocalRandom.current().nextLong(0, (long) 1E10);
        DeviceSignal signal = new DeviceSignal(name, signalId);
        signalReceiver.tell(signal, self());
    }
}