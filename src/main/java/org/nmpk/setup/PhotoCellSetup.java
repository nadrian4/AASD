package org.nmpk.setup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.nmpk.environment.EnvironmentSetup;
import org.nmpk.household.relocation.PhotoCellActor;

public class PhotoCellSetup {

    public static ActorRef setupCell(ActorSystem system, String cellId) {
        ActorRef cellActor = system.actorOf(Props.create(PhotoCellActor.class, cellId), cellId);
        EnvironmentSetup.setupSignalPhotoCellSensor(system, cellId, cellActor);
        return cellActor;
    }
}
