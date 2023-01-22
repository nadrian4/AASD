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
        system.actorOf(Props.create(PeriodicLightSensor.class), "LightSensor-1");
        system.actorOf(Props.create(PeriodicLightSensor.class), "LightSensor-2");

        // temperature
        system.actorOf(Props.create(PeriodicSignalTemperatureSensor.class), "TemperatureSensor-1");
        system.actorOf(Props.create(PeriodicSignalTemperatureSensor.class), "TemperatureSensor-2");

        // windows

        // photo cells

        log.info("Environment setup finished");
    }

    public static void setupSignalPhotoCellSensor(ActorSystem system, String photoCellId, ActorRef photoCellActor) {
        system.actorOf(Props.create(PeriodicPhotoCellSensor.class, photoCellActor), photoCellId + "-Sensor-1");
        system.actorOf(Props.create(PeriodicPhotoCellSensor.class, photoCellActor), photoCellId + "-Sensor-2");
    }

    public static void setupWindowSensor(ActorSystem system, String windowId, ActorRef windowTracker) {
        system.actorOf(Props.create(PeriodicWindowSensor.class, windowTracker), windowId + "-Sensor-1");
    }

    public static void setupLightSensor(ActorSystem system, String lightId, ActorRef lightTracker) {
        system.actorOf(Props.create(PeriodicLightSensor.class, lightTracker), lightId + "-Sensor-1");
    }

    public static void setupTemperatureSensor(ActorSystem system, String temperatureId, ActorRef temperatureTracker) {
        system.actorOf(Props.create(PeriodicSignalTemperatureSensor.class, temperatureTracker), temperatureId + "-Sensor-1");
    }
}
