package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.nmpk.household.room.BaseRoomActors;
import org.nmpk.household.room.ControlRoomActors;

class RoomSetup {
    public static ControlRoomActors setupRoom(ActorSystem system, String roomId) {
        ActorRef crowdActor = CrowdActorSetup.setupCrowdActor(system, roomId);
        ActorRef windowActor = WindowActorSetup.setupWindowActor(system, roomId);
        ActorRef lightActor = LightActorSetup.setupLightActor(system, roomId);
        ActorRef temperatureActor = TemperatureActorSetup.setupTemperatureActor(system, roomId);
        BaseRoomActors roomActors = BaseRoomActors.builder()
                .crowdActor(crowdActor)
                .windowActor(windowActor)
                .lightActor(lightActor)
                .temperatureActor(temperatureActor)
                .build();
        return ControlRoomActors.builder()
                .stateAggregatorActor(StateAggregatorSetup.setupStateAggregatorActor(system, roomActors, roomId))
                .roomController(RoomControllerSetup.setupRoomController(system, roomActors, roomId))
                .build();
    }
}
