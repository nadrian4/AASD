package org.nmpk.household;

import akka.actor.AbstractFSM;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractHouseholdActorWithFSM<S, D> extends AbstractFSM<S, D> {
}
