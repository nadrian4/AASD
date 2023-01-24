package org.nmpk.household.room.temperature;

import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import org.nmpk.household.AbstractHouseholdActor;
import org.nmpk.household.room.command.TemperatureChangeCommand;
import org.nmpk.household.room.command.TemperatureCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class TemperatureActor extends AbstractHouseholdActor {
    private final Logger log;
    private final TemperatureController temperatureController;
    boolean isOpen;
    private final String temperatureReaderId;
    private NewTemperatureRead currentRead;
    private final Set<ActorRef> subscribers;

    public TemperatureActor(String temperatureReaderId, TemperatureController temperatureController) {
        this.log = LoggerFactory.getLogger(TemperatureActor.class.getName() + "-" + temperatureReaderId);
        this.temperatureReaderId = temperatureReaderId;
        this.currentRead = NewTemperatureRead.empty();
        this.temperatureController = temperatureController;
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
                .match(TemperatureChangeCommand.class, temperatureChangeCommand -> {
                    if (temperatureChangeCommand.getCommandType() == TemperatureCommandType.lower) {
                        temperatureController.lower(temperatureChangeCommand.getDelta());
                    } else {
                        temperatureController.raise(temperatureChangeCommand.getDelta());
                    }
                })
                ;
    }
}
