package org.chiclepad.backend.entity;

import java.time.LocalDate;
import java.util.List;

/**
 * One day worth of user diary content
 */
public class DiaryPage extends Entry {

    /**
     * Unique diary entry id
     */
    private final int id;

    /**
     * Content the user added this day
     */
    private String text;

    /**
     * Day the user wrote diary entry
     * This time is different from the time of creation of entry, as this one is a client-side time of creation
     * as opposed to database time of writing entry
     */
    private final LocalDate recordedDay;

    /**
     * Basic constructor
     */
    public DiaryPage(int entryId,
                     List<Category> categories,
                     int id, String text,
                     LocalDate recordedDay) {
        super(entryId, categories);
        this.id = id;
        this.text = text;
        this.recordedDay = recordedDay;
    }

    /**
     * Basic constructor
     */
    public DiaryPage(int entryId, int id, String text, LocalDate recordedDay) {
        super(entryId);
        this.id = id;
        this.text = text;
        this.recordedDay = recordedDay;
    }

    /**
     * @return Unique id of the diary entry
     */
    public int getId() {
        return id;
    }

    /**
     * @return Content of the diary for today
     */
    public String getText() {
        return text;
    }

    /**
     * @param text Change the content of the diary
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return The day the diary entry was written
     */
    public LocalDate getRecordedDay() {
        return recordedDay;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null &&
                getClass() == o.getClass() &&
                id == ((DiaryPage) o).id &&
                entryId == ((DiaryPage) o).entryId;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "DiaryPage{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", recordedDay=" + recordedDay +
                ", entryId=" + entryId +
                '}';
    }

}
