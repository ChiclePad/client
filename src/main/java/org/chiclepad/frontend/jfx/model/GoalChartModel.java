package org.chiclepad.frontend.jfx.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.Dao.GoalDao;
import org.chiclepad.backend.business.session.UserSessionManager;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.DayFrequency;
import org.chiclepad.backend.entity.WeekDayFrequency;
import org.chiclepad.constants.DayOfWeek;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class GoalChartModel implements ListModel {

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM");

    private XYChart.Series successChartData;

    private ObservableList<PieChart.Data> dayChartData;

    private BarChart successChart;

    private PieChart dayChart;

    private GoalDao goalDao = DaoFactory.INSTANCE.getGoalDao();

    private int userId = UserSessionManager.INSTANCE.getCurrentUserSession().getUserId();

    public GoalChartModel(BarChart successChart, PieChart dayChart) {
        this.successChart = successChart;
        this.dayChart = dayChart;

        this.successChartData = new XYChart.Series();
        this.dayChartData = FXCollections.observableArrayList();

        this.successChart.setData(FXCollections.observableArrayList(successChartData));
        this.dayChart.setData(dayChartData);

        initialize();
    }

    private void initialize() {
        refreshWithFilter("");
    }

    public void refreshWithFilter(String filter) {
        this.successChartData = new XYChart.Series();
        this.successChart.setData(FXCollections.observableArrayList(successChartData));

        dayChartData.clear();

        DayFrequency recentDays = goalDao.getCompletedGoalsCountOnRecentDays(userId, filter);
        WeekDayFrequency weekDays = goalDao.getCompletedGoalsCountByWeekDay(userId, filter);

        recentDays.forEach((day, count) -> {
            XYChart.Data data = new XYChart.Data(dateFormatter.format(day), count);
            successChartData.getData().add(data);
        });

        Arrays.stream(DayOfWeek.values()).forEach(dayOfWeek -> {
            PieChart.Data data = new PieChart.Data(dayOfWeek.toString(), weekDays.getFrequency(dayOfWeek));
            dayChartData.add(data);
        });
    }

    @Override
    public void filterByCategory(List<Category> categories) {

    }

    @Override
    public void setCategoryToSelectedEntry(Category category) {

    }

    @Override
    public void clearEntries() {

    }

    @Override
    public void deleteCategoriesForEntry() {

    }

}
