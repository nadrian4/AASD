package org.nmpk.household.room.light;

import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import org.nmpk.household.AbstractHouseholdActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class LightStateActor extends AbstractHouseholdActor {
    private final Logger log;
    boolean isOpen;
    private final String lightId;
    private final Set<ActorRef> subscribers;

    public LightStateActor(String lightId) {
        this.log = LoggerFactory.getLogger(LightStateActor.class.getName() + "-" + lightId);
        this.lightId = lightId;
        this.subscribers = new HashSet<>();
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder
                .match(LightOn.class, w -> {
                    this.isOpen = true;
                    log.debug("light on: {}", lightId);
                    broadcastSwitch(true);
                })
                .match(LightOff.class, w -> {
                    this.isOpen = false;
                    log.debug("light off: {}", lightId);
                    broadcastSwitch(false);
                })
                .match(SubscribeLight.class, subscribeLight -> {
                    log.debug("New subscriber: {}", subscribeLight);
                    this.subscribers.add(subscribeLight.getSubscriber());
                })
                ;
    }

    private void broadcastSwitch(boolean isOn) {
        for (ActorRef subscriber : subscribers) {
            subscriber.tell(new LightSwitched(isOn), self());
        }
    }
}
