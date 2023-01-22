package org.nmpk.household.room.controller;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.FSMStateFunctionBuilder;
import org.nmpk.household.AbstractHouseholdActorWithFSM;
import org.nmpk.household.room.BaseRoomActors;
import org.nmpk.household.room.command.LightTurnCommand;
import org.nmpk.household.room.command.TemperatureChangeCommand;
import org.nmpk.household.room.command.TemperatureCommandType;
import org.nmpk.household.room.command.WindowCommand;
import org.nmpk.household.room.crowd.CrowdState;
import org.nmpk.household.room.crowd.SubscribeCrowd;
import org.nmpk.household.room.light.LightSwitched;
import org.nmpk.household.room.light.SubscribeLight;
import org.nmpk.household.room.temperature.NewTemperatureRead;
import org.nmpk.household.room.temperature.SubscribeTemperature;
import org.nmpk.household.room.window.NewWindowState;
import org.nmpk.household.room.window.SubscribeWindow;
import org.nmpk.timer.PeriodicActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.function.Supplier;

public class RoomController extends AbstractHouseholdActorWithFSM<RoomState, Integer> {
    private final Logger log;
    private final BaseRoomActors roomActors;
    private final ActorRef periodicActor;
    private NewTemperatureRead currentTempRead = NewTemperatureRead.empty();

    RoomController(BaseRoomActors roomActors, String roomId) {
        this.log = LoggerFactory.getLogger(RoomController.class.getName() + "-" + roomId);
        this.roomActors = roomActors;
        Supplier<Long> periodSupplier = () -> Duration.ofSeconds(1).toMillis();
        this.periodicActor = context().system().actorOf(Props.create(PeriodicActor.class, periodSupplier, self()));
        roomActors.getCrowdActor().tell(new SubscribeCrowd(self()), self());
        roomActors.getWindowActor().tell(new SubscribeWindow(self()), self());
        roomActors.getLightActor().tell(new SubscribeLight(self()), self());
        roomActors.getTemperatureActor().tell(new SubscribeTemperature(self()), self());
        initializeStateMachine();
    }

    private void initializeStateMachine() {
        startWith(RoomState.normal, 0);
        when(RoomState.normal, normalMatch()
            .event(PeriodicActor.Tick.class, (tick, __) -> {
                System.out.println("NO TICK PRZYSZED");
                return stay();
            }));
        when(RoomState.highTemperature, normalMatch()
                .event(PeriodicActor.Tick.class, (tick, __) -> {
                    self().tell(new TemperatureChangeCommand(TemperatureCommandType.lower, currentTempRead.getTemperature() * 0.0001d), self());
                    return stay();
                }));
        when(RoomState.lowTemperature, normalMatch()
                .event(PeriodicActor.Tick.class, (tick, __) -> {
                    self().tell(new TemperatureChangeCommand(TemperatureCommandType.raise, currentTempRead.getTemperature() * 0.0001d), self());
                    return stay();
                })
        );
        onTransition(matchState(RoomState.highTemperature, RoomState.normal, () -> {
            log.info("Temperature is normal now: {}", currentTempRead);
            periodicActor.tell(new PeriodicActor.End(), self());
        }));
        onTransition(matchState(RoomState.lowTemperature, RoomState.normal, () -> {
            log.info("Temperature is normal now: {}", currentTempRead);
            periodicActor.tell(new PeriodicActor.End(), self());
        }));
        onTransition(matchState(RoomState.normal, RoomState.highTemperature, () -> {
            log.info("Temperature is high now: {}", currentTempRead);
            periodicActor.tell(new PeriodicActor.FirstTick(), self());
        }));
        onTransition(matchState(RoomState.normal, RoomState.highTemperature, () -> {
            log.info("Temperature is low now: {}", currentTempRead);
            periodicActor.tell(new PeriodicActor.FirstTick(), self());
        }));
        initialize();
        this.periodicActor.tell(new PeriodicActor.End(), self());
    }

    private FSMStateFunctionBuilder<RoomState, Integer> normalMatch() {
        return matchEvent(LightTurnCommand.class, (msg, __) -> {
            onLightTurnCommand(msg);
            return stay();
        })
                .event(TemperatureChangeCommand.class, (msg, __) -> {
                    onTemperatureChangeCommand(msg);
                    return stay();
                })
                .event(WindowCommand.class, (msg, __) -> {
                    onWindowCommand(msg);
                    return stay();
                })
                .event(NewWindowState.class, (msg, __) -> {
                    onNewWindowState(msg);
                    return stay();
                })
                .event(CrowdState.class, (msg, __) -> {
                    onCrowdState(msg);
                    return stay();
                })
                .event(LightSwitched.class, (msg, __) -> {
                    onLightSwitched(msg);
                    return stay();
                })
                .event(NewTemperatureRead.class, (msg, __) -> {
                    onNewTemperature(msg);
                    if (currentTempRead.isHigh()) {
                        return goTo(RoomState.highTemperature);
                    } else if (currentTempRead.isLow()) {
                        return goTo(RoomState.lowTemperature);
                    } else {
                        return goTo(RoomState.normal);
                    }
                });
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
        this.currentTempRead = newTemperatureRead;
    }

    private void onLightTurnCommand(LightTurnCommand command) {
        log.info("New light turn command: {}", command);
    }

    private void onTemperatureChangeCommand(TemperatureChangeCommand command) {
        log.info("New temperature change command: {}", command);
        roomActors.getTemperatureActor().tell(command, self());
    }

    private void onWindowCommand(WindowCommand command) {
        log.info("New window command: {}", command);
    }

}
