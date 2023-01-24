package org.nmpk.household.room.electrics;

import akka.actor.ActorRef;
import org.nmpk.environment.DeviceOffSignal;
import org.nmpk.environment.DeviceOnSignal;
import org.nmpk.environment.DeviceSignal;
import org.nmpk.household.AbstractHouseholdActorWithFSM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public class DeviceChangeTracker extends AbstractHouseholdActorWithFSM<DeviceState, Integer> {
    private final Logger log;
    private final ActorRef deviceStateActor;
    private final String deviceId;

    DeviceChangeTracker(String deviceId, ActorRef deviceStateActor) {
        this.log = LoggerFactory.getLogger(DeviceChangeTracker.class.getName() + "-" + deviceId);
        this.deviceId = deviceId;
        this.deviceStateActor = deviceStateActor;
        initializeStateMachine();
    }

    private void initializeStateMachine() {
        DeviceState initialState = ThreadLocalRandom.current().nextBoolean() ? DeviceState.on : DeviceState.off;
        log.info("Device {} starting with state {}", deviceId, initialState);
        startWith(initialState, 0);
        when(DeviceState.off, matchEvent(
                DeviceSignal.class, (c, d) -> {
                    if (c.getValue() % 500 == 0) {
                        sendDeviceOn();
                        return goTo(DeviceState.on);
                    }
                    return stay();
                }
        ).event(DeviceOnSignal.class, (c, d) -> {
            sendDeviceOn();
            return goTo(DeviceState.on);
        })
                .event(DeviceOffSignal.class, (c, d) -> stay()));
        when(DeviceState.on, matchEvent(
                DeviceSignal.class, (c, d) -> {
                    generatePowerConsumptionRead(c);
                    if (c.getValue() % 500 == 0) {
                        sendDeviceOff();
                        return goTo(DeviceState.off);
                    }
                    return stay();
                }
        ).event(DeviceOnSignal.class, (c, d) -> stay())
                .event(DeviceOffSignal.class, (c, d) -> {
                    sendDeviceOff();
                    return goTo(DeviceState.off);
                }));
        initialize();
    }

    private void sendDeviceOff() {
        deviceStateActor.tell(new DeviceOff(), self());
    }

    private void sendDeviceOn() {
        deviceStateActor.tell(new DeviceOn(), self());
    }

    private void generatePowerConsumptionRead(DeviceSignal signal) {
        deviceStateActor.tell(new PowerConsumptionRead((signal.getValue() % 10) / 1000d, deviceId), self());
    }
}
