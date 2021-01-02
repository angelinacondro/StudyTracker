package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity.SongPlaylistData;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface SongPlaylistDao {
    @Insert(onConflict = REPLACE)
    void insert(SongPlaylistData songPlaylistData);

    @Insert
    void insertAll(SongPlaylistData... songPlaylistData);

    @Delete
    void delete(SongPlaylistData songPlaylistData);

    @Query("DELETE FROM SongPlaylistData")
    void deleteAll();

    @Update
    void update(SongPlaylistData songPlaylistData);

    @Query("SELECT * FROM SongPlaylistData")
    LiveData<List<SongPlaylistData>> getAll();


}
