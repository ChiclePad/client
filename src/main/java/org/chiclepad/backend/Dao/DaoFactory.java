package org.chiclepad.backend.Dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory providing data access objects for manipulation with entities
 */
public enum DaoFactory {

    /**
     * Singleton instance of the factory
     */
    INSTANCE;

    /**
     * Logger for reporting errors, and important events
     */
    private final Logger logger = LoggerFactory.getLogger(DaoFactory.class);

    /**
     *
     */
    private DaoFactory() {
        // TODO get dao instances.. or use Singletons
    }

}
