package org.nmpk.household.room.electrics;

import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import org.nmpk.household.AbstractHouseholdActor;
import org.nmpk.household.room.command.DeviceCommand;
import org.nmpk.household.room.command.DeviceCommandType;
import org.nmpk.household.room.temperature.TemperatureActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class DeviceActor extends AbstractHouseholdActor {
    private final Logger log;
    private final DeviceController deviceController;
    private final String deviceId;
    private PowerConsumptionRead lastConsumptionRead;
    private final Set<ActorRef> subscribers;

    public DeviceActor(String deviceId, DeviceController deviceController) {
        this.log = LoggerFactory.getLogger(TemperatureActor.class.getName() + "-" + deviceId);
        this.deviceId = deviceId;
        this.lastConsumptionRead = PowerConsumptionRead.empty(deviceId);
        this.deviceController = deviceController;
        this.subscribers = new HashSet<>();
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder
                .match(PowerConsumptionRead.class, powerConsumptionRead -> {
                    this.lastConsumptionRead = powerConsumptionRead;
                    log.debug("Power consumption is: {}", powerConsumptionRead);
                    for (ActorRef subscriber : subscribers) {
                        subscriber.tell(powerConsumptionRead, self());
                    }
                })
                .match(SubscribeDevice.class, subscribeDevice -> {
                    log.debug("New subscriber: {}", subscribeDevice);
                    this.subscribers.add(subscribeDevice.getSubscriber());
                })
                .match(DeviceCommand.class, deviceCommand -> {
                    if (deviceCommand.getCommandType() == DeviceCommandType.turnOff) {
                        deviceController.turnOff();
                    } else {
                        deviceController.turnOn();
                    }
                })
                .match(DeviceOff.class, deviceOff -> {
                    log.info("Device turned off");
                })
                .match(DeviceOn.class, deviceOn -> {
                    log.info("Device turned on");
                })
                ;
    }
}
