package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.nmpk.household.room.BaseRoomActors;
import org.nmpk.household.room.RoomActors;

import java.util.List;

class RoomSetup {
    public static RoomActors setupRoom(ActorSystem system, String roomId, List<String> deviceIds) {
        ActorRef crowdActor = CrowdActorSetup.setupCrowdActor(system, roomId);
        ActorRef windowActor = WindowActorSetup.setupWindowActor(system, roomId);
        ActorRef lightActor = LightActorSetup.setupLightActor(system, roomId);
        ActorRef temperatureActor = TemperatureActorSetup.setupTemperatureActor(system, roomId);
        ActorRef devicesActor = DevicesActorSetup.setupDevicesActor(system, roomId, deviceIds);
        BaseRoomActors roomActors = BaseRoomActors.builder()
                .crowdActor(crowdActor)
                .windowActor(windowActor)
                .lightActor(lightActor)
                .temperatureActor(temperatureActor)
                .devicesActor(devicesActor)
                .build();
        return RoomActors.builder()
                .stateAggregatorActor(StateAggregatorSetup.setupStateAggregatorActor(system, roomActors, roomId))
                .roomController(RoomControllerSetup.setupRoomController(system, roomActors, roomId))
                .build();
    }
}
