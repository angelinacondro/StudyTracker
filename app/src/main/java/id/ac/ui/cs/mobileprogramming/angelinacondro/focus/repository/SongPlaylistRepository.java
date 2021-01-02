package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity.SongPlaylistData;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class SongPlaylistRepository {

    static SongPlaylistRepository instance;
    private Application application;
    private FirebaseAuth firebaseAuth;
    private LiveData<List<SongPlaylistData>> allDatas;

    public static SongPlaylistRepository getInstance(){
        if(instance == null){
            instance = new SongPlaylistRepository();
        }
        return instance;
    }

//    public LiveData<List<SongPlaylistData>> getAllDatas(){
//        LiveData<List<SongPlaylistData>> datas = new LiveData<List<SongPlaylistData>>() {
//        }
//    }

    public SongPlaylistRepository(){
        firebaseAuth = FirebaseAuth.getInstance();
        allDatas = new MutableLiveData<>();

    }

//    public void insert (SongPlaylistData songPlaylistData){
//        new InsertDataAsyncTask(songPlaylistDao).execute(songPlaylistData);
//    }
//
//    public void update(SongPlaylistData songPlaylistData){
//        new UpdateDataAsyncTask(songPlaylistDao).execute(songPlaylistData);
//    }
//
//    public void delete(SongPlaylistData songPlaylistData){
//        new DeleteDataAsyncTask(songPlaylistDao).execute(songPlaylistData);
//    }
//
//    public void deleteAllDatas(){
//        new DeleteAllDataAsyncTask(songPlaylistDao).execute();
//
//    }
//
//    public LiveData<List<SongPlaylistData>> getAllDatas(){
//        return allDatas;
//    }
//
//    private static class InsertDataAsyncTask extends AsyncTask<SongPlaylistData, Void, Void>{
//        private SongPlaylistDao songPLaylistDao;
//
//        private InsertDataAsyncTask(SongPlaylistDao songPLaylistDao){
//            this.songPLaylistDao = songPLaylistDao;
//        }
//        @Override
//        protected Void doInBackground(SongPlaylistData... songPlaylistData) {
//            songPLaylistDao.insert(songPlaylistData[0]);
//            return null;
//        }
//    }
//
//    private static class UpdateDataAsyncTask extends AsyncTask<SongPlaylistData, Void, Void>{
//        private SongPlaylistDao songPLaylistDao;
//
//        private UpdateDataAsyncTask(SongPlaylistDao songPLaylistDao){
//            this.songPLaylistDao = songPLaylistDao;
//        }
//        @Override
//        protected Void doInBackground(SongPlaylistData... songPlaylistData) {
//            songPLaylistDao.update(songPlaylistData[0]);
//            return null;
//        }
//    }
//
//    private static class DeleteDataAsyncTask extends AsyncTask<SongPlaylistData, Void, Void>{
//        private SongPlaylistDao songPLaylistDao;
//
//        private DeleteDataAsyncTask(SongPlaylistDao songPLaylistDao){
//            this.songPLaylistDao = songPLaylistDao;
//        }
//        @Override
//        protected Void doInBackground(SongPlaylistData... songPlaylistData) {
//            songPLaylistDao.delete(songPlaylistData[0]);
//            return null;
//        }
//    }
//
//    private static class DeleteAllDataAsyncTask extends AsyncTask<Void, Void, Void>{
//        private SongPlaylistDao songPLaylistDao;
//
//        private DeleteAllDataAsyncTask(SongPlaylistDao songPLaylistDao){
//            this.songPLaylistDao = songPLaylistDao;
//        }
//        @Override
//        protected Void doInBackground(Void... voids) {
//            songPLaylistDao.deleteAll();
//            return null;
//        }
//    }
}
