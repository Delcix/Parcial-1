package com.example.examenparcial1.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ActivityDao {
    @Insert
    long insert(ActivityRecord record);

    @Update
    int update(ActivityRecord record);

    @Delete
    int delete(ActivityRecord record);

    @Query("SELECT * FROM activity_records ORDER BY id DESC")
    List<ActivityRecord> getAll();

    @Query("SELECT * FROM activity_records WHERE categoriaGasto = :categoria ORDER BY id DESC")
    List<ActivityRecord> getByCategory(String categoria);

    @Query("SELECT * FROM activity_records WHERE id = :id LIMIT 1")
    ActivityRecord getById(int id);

    @Query("SELECT IFNULL(SUM(montoGasto), 0) FROM activity_records")
    double getTotalExpenses();

    @Query("SELECT IFNULL(SUM(montoGasto), 0) FROM activity_records WHERE categoriaGasto = :categoria")
    double getTotalByCategory(String categoria);
}
