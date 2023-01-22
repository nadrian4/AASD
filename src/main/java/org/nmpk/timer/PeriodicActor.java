package org.nmpk.timer;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class PeriodicActor extends AbstractActorWithTimers {
    private static Object TICK_KEY = "TickKey";
    private static Object START_KEY = "StartKey";

    public static final class End {}
    public static final class Tick {}
    public static final class FirstTick{}

    private ActorRef consumer;
    private Supplier<Long> periodSupplier;

    public PeriodicActor(Supplier<Long> periodSupplier, ActorRef consumer) {
        this.consumer = consumer;
        this.periodSupplier = periodSupplier;
        getTimers().startSingleTimer(START_KEY, new FirstTick(),
                Duration.create(this.periodSupplier.get(), TimeUnit.MILLISECONDS));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(FirstTick.class, message -> {
                    consumer.tell(new Tick(), self());
                    getTimers().startPeriodicTimer(TICK_KEY, new Tick(),
                            Duration.create(periodSupplier.get(), TimeUnit.MILLISECONDS));
                })
                .match(Tick.class, message -> {
                    consumer.tell(message, self());

                })
                .match(End.class, message -> {
                    getTimers().cancel(TICK_KEY);
                })
                .build();
    }
}
