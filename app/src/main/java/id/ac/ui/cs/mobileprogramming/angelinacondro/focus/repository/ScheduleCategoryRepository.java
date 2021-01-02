package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.RoomDB;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.dao.ScheduleCategoryDao;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity.ScheduleCategoryData;

import java.util.List;

public class ScheduleCategoryRepository {
    private ScheduleCategoryDao scheduleCategoryDao;
    private LiveData<List<ScheduleCategoryData>> allDatas;

    public ScheduleCategoryRepository(Application application){
        RoomDB database = RoomDB.getInstance(application);
        scheduleCategoryDao = database.scheduleCategoryDao();
        allDatas = scheduleCategoryDao.getAll();
    }

    public void insert (ScheduleCategoryData scheduleCategoryData){
        new ScheduleCategoryRepository.InsertDataAsyncTask(scheduleCategoryDao).execute(scheduleCategoryData);
    }

    public void update(ScheduleCategoryData scheduleCategoryData){
        new ScheduleCategoryRepository.UpdateDataAsyncTask(scheduleCategoryDao).execute(scheduleCategoryData);
    }

    public void delete(ScheduleCategoryData scheduleCategoryData){
        new ScheduleCategoryRepository.DeleteDataAsyncTask(scheduleCategoryDao).execute(scheduleCategoryData);
    }

    public void deleteAllDatas(){
        new ScheduleCategoryRepository.DeleteAllDataAsyncTask(scheduleCategoryDao).execute();

    }

    public LiveData<List<ScheduleCategoryData>> getAllDatas(){
        return allDatas;
    }

    private static class InsertDataAsyncTask extends AsyncTask<ScheduleCategoryData, Void, Void> {
        private ScheduleCategoryDao scheduleCategoryDao;

        private InsertDataAsyncTask(ScheduleCategoryDao scheduleCategoryDao){
            this.scheduleCategoryDao = scheduleCategoryDao;
        }
        @Override
        protected Void doInBackground(ScheduleCategoryData... scheduleCategoryData) {
            scheduleCategoryDao.insert(scheduleCategoryData[0]);
            return null;
        }
    }

    private static class UpdateDataAsyncTask extends AsyncTask<ScheduleCategoryData, Void, Void>{
        private ScheduleCategoryDao scheduleCategoryDao;

        private UpdateDataAsyncTask(ScheduleCategoryDao scheduleCategoryDao){
            this.scheduleCategoryDao = scheduleCategoryDao;
        }
        @Override
        protected Void doInBackground(ScheduleCategoryData... scheduleCategoryData) {
            scheduleCategoryDao.update(scheduleCategoryData[0]);
            return null;
        }
    }

    private static class DeleteDataAsyncTask extends AsyncTask<ScheduleCategoryData, Void, Void>{
        private ScheduleCategoryDao scheduleCategoryDao;

        private DeleteDataAsyncTask(ScheduleCategoryDao scheduleCategoryDao){
            this.scheduleCategoryDao = scheduleCategoryDao;
        }
        @Override
        protected Void doInBackground(ScheduleCategoryData... scheduleCategoryData) {
            scheduleCategoryDao.delete(scheduleCategoryData[0]);
            return null;
        }
    }

    private static class DeleteAllDataAsyncTask extends AsyncTask<Void, Void, Void>{
        private ScheduleCategoryDao scheduleCategoryDao;

        private DeleteAllDataAsyncTask(ScheduleCategoryDao scheduleCategoryDao){
            this.scheduleCategoryDao = scheduleCategoryDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            scheduleCategoryDao.deleteAll();
            return null;
        }
    }
}
