package org.chiclepad.backend.entity;

import org.chiclepad.constants.DayOfWeek;

import java.util.HashMap;
import java.util.Map;

/**
 * Frequency of an event each week day
 */
public class WeekDayFrequency {

    /**
     * Map storing frequencies
     */
    private Map<DayOfWeek, Integer> frequency;

    /**
     * Basic constructor
     */
    public WeekDayFrequency() {
        frequency = new HashMap<>(7);

        for (DayOfWeek day : DayOfWeek.values()) {
            frequency.put(day, 0);
        }
    }

    /**
     * @return Number of events for the day
     */
    public int getFrequency(DayOfWeek day) {
        return frequency.get(day);
    }

    /**
     * Sets non-negative number of events for that day of week
     */
    public void setFrequency(DayOfWeek day, int frequency) throws IllegalArgumentException {
        if (frequency < 0) {
            throw new IllegalArgumentException("Can't use negative value " + frequency + " as a frequency.");
        }

        this.frequency.put(day, frequency);
    }

}
