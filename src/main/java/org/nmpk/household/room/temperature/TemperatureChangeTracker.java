package org.nmpk.household.room.temperature;

import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import org.nmpk.environment.TemperatureLowered;
import org.nmpk.environment.TemperatureRaised;
import org.nmpk.environment.TemperatureSignal;
import org.nmpk.household.AbstractHouseholdActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public class TemperatureChangeTracker extends AbstractHouseholdActor {
    private static final double maxTemp = 24;
    private static final double minTemp = 16;
    private final Logger log;
    private final ActorRef temperatureActor;
    private final long sequence;
    private double lastRead = (maxTemp - minTemp) / 2.0;

    TemperatureChangeTracker(String temperatureReaderId, ActorRef temperatureActor) {
        this.log = LoggerFactory.getLogger(TemperatureChangeTracker.class.getName() + "-" + temperatureReaderId);
        this.temperatureActor = temperatureActor;
        this.sequence = 0L;
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder
                .match(TemperatureSignal.class, temperatureSignal -> {
                    double remainder = temperatureSignal.getValue() % 10;
                    if (remainder == 1 || remainder == 3 || remainder == 5) {
                        int changeDirection = ThreadLocalRandom.current().nextLong() % 2 == 0   ? -1 : 1;
                        double multiplier = ThreadLocalRandom.current().nextDouble(0, 1) ;
                        double change = (maxTemp - minTemp) * multiplier * (remainder % 10) / 100.0;
                        lastRead = Math.max(Math.min(lastRead + changeDirection * change, maxTemp), minTemp);
                        broadcastTemperature();
                    }
                })
                .match(TemperatureRaised.class, temperatureRaised -> {
                    log.info("Raised by {}", temperatureRaised.getDelta());
                    lastRead = lastRead + temperatureRaised.getDelta();
                    broadcastTemperature();
                })
                .match(TemperatureLowered.class, temperatureLowered -> {
                    log.info("Lowered by {}", temperatureLowered.getDelta());
                    lastRead = lastRead - temperatureLowered.getDelta();
                    broadcastTemperature();
                })
                ;
    }

    private void broadcastTemperature() {
        temperatureActor.tell(new NewTemperatureRead(lastRead), self());
    }
}
