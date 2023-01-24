package org.nmpk.environment;

import akka.actor.ActorRef;
import lombok.Data;
import org.nmpk.household.room.electrics.DeviceController;

import java.util.Optional;

@Data
public class BuiltInDeviceController implements DeviceController {
    Optional<ActorRef> deviceTracker;

    @Override
    public void turnOn() {
        deviceTracker.ifPresent(t -> t.tell(new DeviceOnSignal(), ActorRef.noSender()));
    }

    @Override
    public void turnOff() {
        deviceTracker.ifPresent(t -> t.tell(new DeviceOffSignal(), ActorRef.noSender()));
    }
}
