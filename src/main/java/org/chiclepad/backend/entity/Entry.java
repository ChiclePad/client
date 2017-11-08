package org.chiclepad.backend.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 */
public abstract class Entry {

    private final LocalDateTime created;

    private final List<Category> categories;

}
