package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.nmpk.environment.EnvironmentSetup;
import org.nmpk.household.room.window.WindowChangeTracker;
import org.nmpk.household.room.window.WindowStateActor;

class WindowActorSetup {
    static ActorRef setupWindowActor(ActorSystem system, String roomId) {
        String windowId = roomId + "-Window1";
        ActorRef windowStateActor = system.actorOf(Props.create(WindowStateActor.class, windowId), "WindowStateActor-" + windowId);
        ActorRef changeTracker = system.actorOf(Props.create(WindowChangeTracker.class, windowId, windowStateActor), "WindowChangeTracker-" + windowId);
        EnvironmentSetup.setupWindowSensor(system, windowId, changeTracker);
        return windowStateActor;
    }
}
