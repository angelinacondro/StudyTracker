package com.angelinacondro.studytracker.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.angelinacondro.studytracker.db.dao.ScheduleDao;
import com.angelinacondro.studytracker.db.entity.ScheduleData;
import com.angelinacondro.studytracker.db.RoomDB;

import java.util.List;

public class ScheduleRepository {
    private ScheduleDao scheduleDao;
    private LiveData<List<ScheduleData>> allDatas;

    public ScheduleRepository(Application application){
        RoomDB database = RoomDB.getInstance(application);
        scheduleDao = database.mainDao();
        allDatas = scheduleDao.getAll();
    }

    public void insert (ScheduleData scheduleData){
        new InsertDataAsyncTask(scheduleDao).execute(scheduleData);
    }

    public void update(ScheduleData scheduleData){
        new UpdateDataAsyncTask(scheduleDao).execute(scheduleData);
    }

    public void delete(ScheduleData scheduleData){
        new DeleteDataAsyncTask(scheduleDao).execute(scheduleData);
    }

    public void deleteAllDatas(){
        new DeleteAllDataAsyncTask(scheduleDao).execute();

    }

    public LiveData<List<ScheduleData>> getAllDatas(){
        return allDatas;
    }

    private static class InsertDataAsyncTask extends AsyncTask<ScheduleData, Void, Void>{
        private ScheduleDao scheduleDao;

        private InsertDataAsyncTask(ScheduleDao scheduleDao){
            this.scheduleDao = scheduleDao;
        }
        @Override
        protected Void doInBackground(ScheduleData... scheduleData) {
            scheduleDao.insert(scheduleData[0]);
            return null;
        }
    }

    private static class UpdateDataAsyncTask extends AsyncTask<ScheduleData, Void, Void>{
        private ScheduleDao scheduleDao;

        private UpdateDataAsyncTask(ScheduleDao scheduleDao){
            this.scheduleDao = scheduleDao;
        }
        @Override
        protected Void doInBackground(ScheduleData... scheduleData) {
            scheduleDao.update(scheduleData[0]);
            return null;
        }
    }

    private static class DeleteDataAsyncTask extends AsyncTask<ScheduleData, Void, Void>{
        private ScheduleDao scheduleDao;

        private DeleteDataAsyncTask(ScheduleDao scheduleDao){
            this.scheduleDao = scheduleDao;
        }
        @Override
        protected Void doInBackground(ScheduleData... scheduleData) {
            scheduleDao.delete(scheduleData[0]);
            return null;
        }
    }

    private static class DeleteAllDataAsyncTask extends AsyncTask<Void, Void, Void>{
        private ScheduleDao scheduleDao;

        private DeleteAllDataAsyncTask(ScheduleDao scheduleDao){
            this.scheduleDao = scheduleDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            scheduleDao.deleteAll();
            return null;
        }
    }
}
