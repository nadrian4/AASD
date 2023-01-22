package org.nmpk.environment;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
class PeriodicSignalLightSensor extends PeriodicSignalSensor {
    PeriodicSignalLightSensor() {
        super(new RandomEmitIntervalSupplier(200));
    }

    @Override
    void emitSignal() {
        log.info("New light signal! {}", ThreadLocalRandom.current().nextInt());
    }
}
