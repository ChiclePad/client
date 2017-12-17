package org.chiclepad.constants;

import java.util.Arrays;

public enum DayOfWeek {

    MONDAY("Mon", 1),
    TUESDAY("Tue", 2),
    WEDNESDAY("Wed", 3),
    THURSDAY("Thr", 4),
    FRIDAY("Fri", 5),
    SATURDAY("Say", 6),
    SUNDAY("Sun", 7);

    private final String dayName;

    private final int dayNumber;

    DayOfWeek(String dayName, int dayNumber) {
        this.dayName = dayName;
        this.dayNumber = dayNumber;
    }

    public static DayOfWeek fromInt(int day) throws IllegalArgumentException {
        return Arrays.stream(DayOfWeek.values())
                .filter(dayOfWeek -> dayOfWeek.dayNumber == day)
                .findFirst()
                .orElseThrow(() -> {
                    return new IllegalArgumentException("Week days are counted 1 to 7 (Monday to Sunday), but got " + day);
                });
    }

    public static DayOfWeek fromString(String string) throws IllegalArgumentException {
        return Arrays.stream(DayOfWeek.values())
                .filter(dayOfWeek -> dayOfWeek.dayName.equals(string))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Provided string " + string + " is not a day of week."));
    }

    @Override
    public String toString() {
        return dayName;
    }

}
