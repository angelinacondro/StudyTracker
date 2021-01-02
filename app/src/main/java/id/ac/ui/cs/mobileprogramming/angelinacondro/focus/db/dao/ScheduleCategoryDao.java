package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity.ScheduleCategoryData;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ScheduleCategoryDao {
    @Insert(onConflict = REPLACE)
    void insert(ScheduleCategoryData scheduleCategoryData);

    @Insert
    void insertAll(ScheduleCategoryData... scheduleCategoryData);

    @Delete
    void delete(ScheduleCategoryData scheduleCategoryData);

    @Query("DELETE FROM scheduleCategory")
    void deleteAll();

    @Update
    void update(ScheduleCategoryData scheduleCategoryData);

    @Query("SELECT * FROM scheduleCategory")
    LiveData<List<ScheduleCategoryData>> getAll();

//    @Query("SELECT category FROM scheduleCategory")
//    List<String> getAllTeamName();


}
