package org.chiclepad.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
     */
    private final LocalDate recordedDay;

    /**
     * Basic constructor
     */
    public DiaryPage(int entryId,
                     LocalDateTime created,
                     List<Category> categories,
                     int id, String text,
                     LocalDate recordedDay) {
        super(entryId, created, categories);
        this.id = id;
        this.text = text;
        this.recordedDay = recordedDay;
    }

    /**
     * Basic constructor
     */
    public DiaryPage(int entryId, LocalDateTime created, int id, String text, LocalDate recordedDay) {
        super(entryId, created);
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DiaryPage diaryPage = (DiaryPage) o;
        return id == diaryPage.id &&
                recordedDay.equals(diaryPage.recordedDay);
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
