package org.chiclepad.frontend.jfx;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.backend.entity.Todo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO remove after connection with backend
 */
public class MOCKUP {

    public static ChiclePadUser USER;

    public static List<Category> CATEGORIES = new ArrayList<>();

    static {
        USER = new ChiclePadUser(1, "1234", "salt", "Daco", "Alan Turing");

        Todo todo1 = new Todo(
                1,
                LocalDateTime.now(),
                2,
                "Finish the project",
                LocalDateTime.of(2020, 3, 1, 2, 3),
                -1
        );


        Todo todo2 = new Todo(
                2,
                LocalDateTime.now(),
                3,
                "Start exercising",
                LocalDateTime.of(2000, 3, 1, 2, 3),
                10
        );


        Todo todo3 = new Todo(
                3,
                LocalDateTime.of(1990, 2, 1, 4, 56),
                4,
                "Build a cross country highway",
                LocalDateTime.of(2020, 3, 1, 2, 3),
                LocalDateTime.of(2019, 3, 1, 2, 4),
                0
        );

        USER.getEntries().add(todo1);
        USER.getEntries().add(todo2);
        USER.getEntries().add(todo3);

        FontAwesomeIconName[] icons = FontAwesomeIconName.values();
        for (int i = 0; i < 5; i++) {
            CATEGORIES.add(new Category(
                    i,
                    "Category_" + i,
                    icons[(int) (Math.random() * icons.length)].name(),
                    String.format(
                            "#%02x%02x%02x",
                            (int) (Math.random() * 256),
                            (int) (Math.random() * 256),
                            (int) (Math.random() * 256))
            ));
        }
    }

}
