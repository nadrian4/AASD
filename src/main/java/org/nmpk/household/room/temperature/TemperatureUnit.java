package org.nmpk.household.room.temperature;

enum TemperatureUnit {
    CELSIUS {
        @Override
        public String toString() {
            return "°C";
        }
    }
}
