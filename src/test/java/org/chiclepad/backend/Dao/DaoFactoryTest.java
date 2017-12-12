package org.chiclepad.backend.Dao;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DaoFactoryTest {

    @Test
    void getCategoryDao() {
        assertThat(DaoFactory.INSTANCE.getCategoryDao()).isNotNull();
    }

    @Test
    void getChiclePadUserDao() {
        assertThat(DaoFactory.INSTANCE.getChiclePadUserDao()).isNotNull();
    }

    @Test
    void getDiaryPageDao() {
        assertThat(DaoFactory.INSTANCE.getDiaryPageDao()).isNotNull();
    }

    @Test
    void getGoalDao() {
        assertThat(DaoFactory.INSTANCE.getGoalDao()).isNotNull();
    }

    @Test
    void getNoteDao() {
        assertThat(DaoFactory.INSTANCE.getNoteDao()).isNotNull();
    }

    @Test
    void getTodoDao() {
        assertThat(DaoFactory.INSTANCE.getTodoDao()).isNotNull();
    }

}
