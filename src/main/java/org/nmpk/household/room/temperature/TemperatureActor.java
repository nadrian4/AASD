package org.nmpk.household.room.temperature;

import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import akka.protobuf.LazyStringArrayList;
import org.nmpk.household.AbstractHouseholdActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class TemperatureActor extends AbstractHouseholdActor {
    private final Logger log;
    boolean isOpen;
    private final String temperatureReaderId;
    private NewTemperatureRead currentRead;
    private final Set<ActorRef> subscribers;

    public TemperatureActor(String temperatureReaderId) {
        this.log = LoggerFactory.getLogger(TemperatureActor.class.getName() + "-" + temperatureReaderId);
        this.temperatureReaderId = temperatureReaderId;
        this.currentRead = NewTemperatureRead.empty();
        this.subscribers = new HashSet<>();
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder
                .match(NewTemperatureRead.class, newTemperatureRead -> {
                    this.currentRead = newTemperatureRead;
                    log.debug("New temperature is: {}", newTemperatureRead);
                    for (ActorRef subscriber : subscribers) {
                        subscriber.tell(newTemperatureRead, self());
                    }
                })
                .match(SubscribeTemperature.class, subscribeTemperature -> {
                    log.debug("New subscriber: {}", subscribeTemperature);
                    this.subscribers.add(subscribeTemperature.getSubscriber());
                })
                ;
    }
}
