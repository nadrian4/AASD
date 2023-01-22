package org.nmpk.environment;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

class RandomEmitIntervalSupplier implements Supplier<Long> {
    private final long minInterval;
    private final long maxInterval;

    RandomEmitIntervalSupplier(long maxInterval) {
        this(0, maxInterval);
    }

    RandomEmitIntervalSupplier(long minInterval, long maxInterval) {
        if (minInterval >= maxInterval) {
            throw new IllegalArgumentException("minInterval must be less than maxInterval!");
        }
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
    }

    @Override
    public Long get() {
        return ThreadLocalRandom.current().nextLong(minInterval, maxInterval);
    }
}
