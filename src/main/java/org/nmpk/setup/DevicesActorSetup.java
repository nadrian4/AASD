package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.nmpk.environment.BuiltInDeviceController;
import org.nmpk.environment.EnvironmentSetup;
import org.nmpk.household.room.electrics.DeviceActor;
import org.nmpk.household.room.electrics.DevicesActor;
import org.nmpk.household.room.electrics.DeviceChangeTracker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class DevicesActorSetup {
    static ActorRef setupDevicesActor(ActorSystem system, String roomId, List<String> deviceIds) {
        Map<String, ActorRef> devices = new HashMap<>();
        for (String deviceId : deviceIds) {
            String finalDeviceId = roomId + "-" + deviceId;
            BuiltInDeviceController deviceController = new BuiltInDeviceController();
            ActorRef deviceStateActor = system.actorOf(Props.create(DeviceActor.class, finalDeviceId, deviceController), "DeviceStateActor-" + finalDeviceId);
            ActorRef changeTracker = system.actorOf(Props.create(DeviceChangeTracker.class, finalDeviceId, deviceStateActor), "DeviceChangeTracker-" + finalDeviceId);
            deviceController.setDeviceTracker(Optional.of(changeTracker));
            EnvironmentSetup.setupDeviceSensor(system, finalDeviceId, changeTracker);
            devices.put(finalDeviceId, deviceStateActor);
        }
        return system.actorOf(Props.create(DevicesActor.class, roomId, devices), "DevicesActor-" + roomId);
    }
    
}
