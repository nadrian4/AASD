package org.nmpk.environment;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnvironmentSetup {
    public static void setupEnvironment(ActorSystem system) {
        log.info("Start environment setup");
        // light sensor
        system.actorOf(Props.create(PeriodicSignalLightSensor.class), "LightSensor-1");
        system.actorOf(Props.create(PeriodicSignalLightSensor.class), "LightSensor-2");

        // temperature
        system.actorOf(Props.create(PeriodicSignalTemperatureSensor.class), "TemperatureSensor-1");
        system.actorOf(Props.create(PeriodicSignalTemperatureSensor.class), "TemperatureSensor-2");

        // windows
        system.actorOf(Props.create(PeriodicWindowSensor.class), "WindowSensor-1");
        system.actorOf(Props.create(PeriodicWindowSensor.class), "WindowSensor-2");

        // photo cells
        system.actorOf(Props.create(PeriodicSignalPhotoCellSensor.class), "PhotoCell-1.1");
        system.actorOf(Props.create(PeriodicSignalPhotoCellSensor.class), "PhotoCell-1.2");
        system.actorOf(Props.create(PeriodicSignalPhotoCellSensor.class), "PhotoCell-2.1");
        system.actorOf(Props.create(PeriodicSignalPhotoCellSensor.class), "PhotoCell-2.2");

        log.info("Environment setup finished");
    }

    public static void setupSignalPhotoCellSensor(ActorSystem system, String photoCellId, ActorRef photoCellActor) {
        system.actorOf(Props.create(PeriodicSignalPhotoCellSensor.class, photoCellActor), photoCellId + "-Sensor-1");
        system.actorOf(Props.create(PeriodicSignalPhotoCellSensor.class, photoCellActor), photoCellId + "-Sensor-2");
    }

    public static void setupWindowSensor(ActorSystem system, String windowId, ActorRef windowStateActor) {
        system.actorOf(Props.create(PeriodicWindowSensor.class, windowStateActor), windowId + "-Sensor-1");
    }
}
