package org.nmpk.household.room.electrics;

import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import org.nmpk.household.AbstractHouseholdActor;
import org.nmpk.household.room.command.AllDevicesCommand;
import org.nmpk.household.room.command.DeviceCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DevicesActor extends AbstractHouseholdActor {

    private final Map<String, ActorRef> devicesActors;
    private final Logger log;

    DevicesActor(String roomId, Map<String, ActorRef> devicesActors) {
        this.devicesActors = devicesActors;
        this.log = LoggerFactory.getLogger(DevicesActor.class.getName() + "-" + roomId);
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder
                .match(SubscribeAllDevices.class, subscribeAllDevices -> {
                    log.debug("New all devices subscriber: {}", subscribeAllDevices);
                    devicesActors.forEach((id, deviceActor) -> {
                        deviceActor.tell(new SubscribeDevice(subscribeAllDevices.getSubscriber(), id), sender());
                    });
                })
                .match(SubscribeDevice.class, subscribeDevice -> {
                    log.debug("New subscriber: {}", subscribeDevice);
                    if (devicesActors.containsKey(subscribeDevice.getDeviceId())) {
                        devicesActors.get(subscribeDevice.getDeviceId()).tell(subscribeDevice, sender());
                    }
                })
                .match(DeviceCommand.class, deviceCommand -> {
                    log.info("Device command: {}", deviceCommand);
                    if (devicesActors.containsKey(deviceCommand.getDeviceId())) {
                        devicesActors.get(deviceCommand.getDeviceId()).tell(deviceCommand, sender());
                    }
                })
                .match(AllDevicesCommand.class, allDevicesCommand -> {
                    log.info("All devices command {}", allDevicesCommand);
                    devicesActors.forEach((id, deviceActor) -> {
                        deviceActor.tell(new DeviceCommand(allDevicesCommand.getCommandType(), id), sender());
                    });
                })
                ;
    }
}
