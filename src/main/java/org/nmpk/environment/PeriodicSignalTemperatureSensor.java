package org.nmpk.environment;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
class PeriodicSignalTemperatureSensor extends PeriodicSignalSensor implements PhotoCellSensor {

    PeriodicSignalTemperatureSensor() {
        super(new RandomEmitIntervalSupplier(300));
    }

    @Override
    void emitSignal() {
        log.info("New temperature signal! {}", ThreadLocalRandom.current().nextInt());
    }
}