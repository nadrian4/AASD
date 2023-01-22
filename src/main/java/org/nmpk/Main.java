package org.nmpk;

import akka.actor.ActorSystem;
import lombok.extern.slf4j.Slf4j;
import org.nmpk.setup.ApartmentSetup;

@Slf4j
class Main {
    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("PhotoCell");
//        EnvironmentSetup.setupEnvironment(system);
        ApartmentSetup.setupApartment(system);
        log.info("Still running");
    }
}
