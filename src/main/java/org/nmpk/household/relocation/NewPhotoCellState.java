package org.nmpk.household.relocation;

import lombok.Value;

import java.util.Objects;

@Value
class NewPhotoCellState {
    String cellId;
    PhotoCellState newState;

    boolean refersTo(String anotherCellId) {
        return Objects.equals(cellId, anotherCellId);
    }

    boolean isOn() {
        return newState == PhotoCellState.ON;
    }

    boolean isOff() {
        return newState == PhotoCellState.OFF;
    }
}
