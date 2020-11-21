package com.angelinacondro.studytracker.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.angelinacondro.studytracker.db.entity.ScheduleData;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ScheduleDao {
    @Insert(onConflict = REPLACE)
    void insert(ScheduleData scheduleData);

    @Insert
    void insertAll(ScheduleData... scheduleData);

    @Delete
    void delete(ScheduleData scheduleData);

    @Query("DELETE FROM ScheduleData")
    void deleteAll();

    @Update
    void update(ScheduleData scheduleData);

    @Query("SELECT * FROM ScheduleData")
    LiveData<List<ScheduleData>> getAll();


}
