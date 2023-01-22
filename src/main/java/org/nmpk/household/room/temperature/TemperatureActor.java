package org.nmpk.household.room.temperature;

import akka.japi.pf.ReceiveBuilder;
import org.nmpk.household.AbstractHouseholdActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

public class TemperatureActor extends AbstractHouseholdActor {
    private final Logger log;
    boolean isOpen;
    private final String temperatureReaderId;
    private NewTemperatureRead currentRead;

    public TemperatureActor(String temperatureReaderId) {
        this.log = LoggerFactory.getLogger(TemperatureActor.class.getName() + "-" + temperatureReaderId);
        this.temperatureReaderId = temperatureReaderId;
        this.currentRead = NewTemperatureRead.empty();
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder
                .match(NewTemperatureRead.class, newTemperatureRead -> {
                    this.currentRead = newTemperatureRead;
                    log.debug("New temperature is: {}", newTemperatureRead);
                })
                ;
    }
}
