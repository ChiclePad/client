package org.chiclepad.frontend.jfx;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import org.chiclepad.backend.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO remove after connection with backend
 */
public class MOCKUP {

    public static ChiclePadUser USER;

    public static List<Category> CATEGORIES = new ArrayList<>();

    private static int entryId = 1;

    private static int categoryId = 1;

    private static int todoId = 1;

    private static int noteId = 1;

    private static int goalId = 1;

    private static int diaryPageId = 1;

    static {
        USER = new ChiclePadUser(1, "1234", "Daco", "Alan Turing");

        CATEGORIES.add(new Category(categoryId++, "School", FontAwesomeIconName.BOOK.name(), "#EF9A9A"));
        CATEGORIES.add(new Category(categoryId++, "Shopping", FontAwesomeIconName.SHOPPING_CART.name(), "#80DEEA"));
        CATEGORIES.add(new Category(categoryId++, "Work", FontAwesomeIconName.FILE_TEXT.name(), "#FFB74D"));
        CATEGORIES.add(new Category(categoryId++, "Personal", FontAwesomeIconName.USER.name(), "#BCAAA4"));
        CATEGORIES.add(new Category(categoryId++, "Finance", FontAwesomeIconName.USD.name(), "#CE93D8"));

        Todo todo1 = new Todo(entryId++, LocalDateTime.now(), todoId++, "Finish the project", LocalDateTime.of(2020, 3, 1, 2, 3), -1);
        Todo todo2 = new Todo(entryId++, LocalDateTime.now(), List.of(CATEGORIES.get(0), CATEGORIES.get(1)), todoId++, "Start exercising", LocalDateTime.of(2000, 3, 1, 2, 3), 10);
        Todo todo3 = new Todo(entryId++, LocalDateTime.of(1990, 2, 1, 4, 56), todoId++, "Build a cross country highway", LocalDateTime.of(2020, 3, 1, 2, 3), LocalDateTime.of(2019, 3, 1, 2, 4), 0);

        Note note1 = new Note(entryId++, LocalDateTime.now(), noteId++, 10, 20, "You can do it");
        Note note2 = new Note(entryId++, LocalDateTime.now(), List.of(CATEGORIES.get(0), CATEGORIES.get(1)), noteId++, 100, 200, "Start bodybuilding");
        Note note3 = new Note(entryId++, LocalDateTime.of(1990, 2, 1, 4, 56), noteId++, -1, -1, "Don't be a lazy !@#$");
        Note note4 = new Note(entryId++, LocalDateTime.of(1990, 2, 1, 4, 56), List.of(CATEGORIES.get(4)), noteId++, -1, -1, "And buy some milk");

        Goal goal1 = new Goal(entryId++, goalId++, "Stretch for 10 minutes");
        Goal goal2 = new Goal(entryId++, List.of(CATEGORIES.get(0), CATEGORIES.get(1)), goalId++, "Wake up on time", List.of(new CompletedGoal(1, LocalDate.now().plusDays(10), LocalTime.now()), new CompletedGoal(2, LocalDate.now().plusDays(11), LocalTime.now().plusHours(2))));
        Goal goal3 = new Goal(entryId++, goalId++, "Eat 1 roll less each week");
        Goal goal4 = new Goal(entryId++, List.of(CATEGORIES.get(4)), goalId++, "Read at least 20 pages of a book", List.of(new CompletedGoal(3, LocalDate.of(1990, 2, 1).plusDays(3), LocalTime.now())));

        DiaryPage diaryPage1 = new DiaryPage(entryId++, diaryPageId++, "Dear diary, today I slept well.", LocalDate.now());
        DiaryPage diaryPage2 = new DiaryPage(entryId++, diaryPageId++, "My dear reader have a great day.", LocalDate.now());
        DiaryPage diaryPage3 = new DiaryPage(entryId++, diaryPageId++, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", LocalDate.of(1990, 2, 1));
        DiaryPage diaryPage4 = new DiaryPage(entryId++, List.of(CATEGORIES.get(4)), diaryPageId++, "Dear diary, today I !@#$%^ up Lorem Ipsum", LocalDate.of(1990, 2, 1));


        USER.getEntries().add(todo1);
        USER.getEntries().add(todo2);
        USER.getEntries().add(todo3);

        USER.getEntries().add(note1);
        USER.getEntries().add(note2);
        USER.getEntries().add(note3);
        USER.getEntries().add(note4);

        USER.getEntries().add(goal1);
        USER.getEntries().add(goal2);
        USER.getEntries().add(goal3);
        USER.getEntries().add(goal4);

        USER.getEntries().add(diaryPage1);
        USER.getEntries().add(diaryPage2);
        USER.getEntries().add(diaryPage3);
        USER.getEntries().add(diaryPage4);

    }

}
