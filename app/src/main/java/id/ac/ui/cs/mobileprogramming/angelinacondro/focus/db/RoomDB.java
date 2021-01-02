package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.dao.ScheduleCategoryDao;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.dao.ScheduleDao;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.dao.SongPlaylistDao;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity.ScheduleCategoryData;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity.ScheduleData;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity.SongPlaylistData;

@Database(entities = {ScheduleData.class, ScheduleCategoryData.class, SongPlaylistData.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;
    private static String DATABASE_NAME = "database";


    public synchronized static RoomDB getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration().addCallback(roomCallback).build();
        }
        return database;
    }

    public abstract ScheduleDao mainDao();

    public abstract ScheduleCategoryDao scheduleCategoryDao();

    public abstract SongPlaylistDao songPlaylistDao();

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(database).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void,Void> {
        private ScheduleDao scheduleDao;
        private ScheduleCategoryDao scheduleCategoryDao;
        private SongPlaylistDao songPlaylistDao;

        private PopulateDbAsyncTask(RoomDB db){
            scheduleDao = db.mainDao();
            scheduleCategoryDao = db.scheduleCategoryDao();
            songPlaylistDao = db.songPlaylistDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            scheduleDao.insert(new ScheduleData());
//            scheduleCategoryDao.insert(new ScheduleCategoryData());
            return null;
        }
    }
}
