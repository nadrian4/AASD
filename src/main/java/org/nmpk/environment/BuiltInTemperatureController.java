package org.nmpk.environment;

import akka.actor.ActorRef;
import lombok.Data;
import org.nmpk.household.room.temperature.TemperatureController;

import java.util.Optional;


@Data
public class BuiltInTemperatureController implements TemperatureController {
    Optional<ActorRef> temperatureTracker;

    @Override
    public void raise(double delta) {
        temperatureTracker.ifPresent(t -> t.tell(new TemperatureRaised(delta), ActorRef.noSender()));
    }

    @Override
    public void lower(double delta) {
        temperatureTracker.ifPresent(t -> t.tell(new TemperatureLowered(delta), ActorRef.noSender()));
    }
}
