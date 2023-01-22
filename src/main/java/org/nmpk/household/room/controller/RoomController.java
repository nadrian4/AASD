package org.nmpk.household.room.controller;

import akka.japi.pf.ReceiveBuilder;
import org.nmpk.household.AbstractHouseholdActor;
import org.nmpk.household.room.BaseRoomActors;
import org.nmpk.household.room.command.LightTurnCommand;
import org.nmpk.household.room.command.TemperatureChangeCommand;
import org.nmpk.household.room.command.WindowCommand;
import org.nmpk.household.room.crowd.CrowdState;
import org.nmpk.household.room.crowd.SubscribeCrowd;
import org.nmpk.household.room.light.LightSwitched;
import org.nmpk.household.room.light.SubscribeLight;
import org.nmpk.household.room.temperature.NewTemperatureRead;
import org.nmpk.household.room.temperature.SubscribeTemperature;
import org.nmpk.household.room.window.NewWindowState;
import org.nmpk.household.room.window.SubscribeWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoomController extends AbstractHouseholdActor {
    private final Logger log;
    private final BaseRoomActors roomActors;

    RoomController(BaseRoomActors roomActors, String roomId) {
        this.log = LoggerFactory.getLogger(RoomController.class.getName() + "-" + roomId);
        this.roomActors = roomActors;
        roomActors.getCrowdActor().tell(new SubscribeCrowd(self()), self());
        roomActors.getWindowActor().tell(new SubscribeWindow(self()), self());
        roomActors.getLightActor().tell(new SubscribeLight(self()), self());
        roomActors.getTemperatureActor().tell(new SubscribeTemperature(self()), self());
    }

    @Override
    protected ReceiveBuilder addToReceive(ReceiveBuilder builder) {
        return builder
                .match(LightTurnCommand.class, this::onLightTurnCommand)
                .match(TemperatureChangeCommand.class, this::onTemperatureChangeCommand)
                .match(WindowCommand.class, this::onWindowCommand)
                .match(NewWindowState.class, this::onNewWindowState)
                .match(CrowdState.class, this::onCrowdState)
                .match(LightSwitched.class, this::onLightSwitched)
                .match(NewTemperatureRead.class, this::onNewTemperature)
                ;
    }

    private void onNewWindowState(NewWindowState newWindowState) {
        log.debug("New window state {}", newWindowState);
    }

    private void onCrowdState(CrowdState crowdState) {
        log.debug("New crowd state {}", crowdState);
    }

    private void onLightSwitched(LightSwitched lightSwitched) {
        log.debug("On light switched {}", lightSwitched);
    }

    private void onNewTemperature(NewTemperatureRead newTemperatureRead) {
        log.debug("New temperature: {}", newTemperatureRead);
    }

    private void onLightTurnCommand(LightTurnCommand command) {
        log.info("New light turn command: {}", command);
    }

    private void onTemperatureChangeCommand(TemperatureChangeCommand command) {
        log.info("New temperature change command: {}", command);
    }

    private void onWindowCommand(WindowCommand command) {
        log.info("New window command: {}", command);
    }

}
