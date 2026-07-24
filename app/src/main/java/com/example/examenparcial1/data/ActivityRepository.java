package com.example.examenparcial1.data;

import android.content.Context;

import java.util.List;

public class ActivityRepository {
    private final ActivityDao activityDao;

    public ActivityRepository(Context context) {
        activityDao = AppDatabase.getInstance(context).activityDao();
    }

    public long insert(ActivityRecord record) {
        return activityDao.insert(record);
    }

    public int update(ActivityRecord record) {
        return activityDao.update(record);
    }

    public int delete(ActivityRecord record) {
        return activityDao.delete(record);
    }

    public List<ActivityRecord> getAll() {
        return activityDao.getAll();
    }

    public List<ActivityRecord> getByCategory(String category) {
        return activityDao.getByCategory(category);
    }

    public ActivityRecord getById(int id) {
        return activityDao.getById(id);
    }

    public double getTotalExpenses() {
        return activityDao.getTotalExpenses();
    }

    public double getTotalByCategory(String category) {
        return activityDao.getTotalByCategory(category);
    }
}
