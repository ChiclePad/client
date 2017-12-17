package org.chiclepad.backend.entity;

import java.time.LocalDate;
import java.util.Map;

/**
 * Frequency of an event each day
 */
public interface DayFrequency extends Map<LocalDate, Integer> {
}
