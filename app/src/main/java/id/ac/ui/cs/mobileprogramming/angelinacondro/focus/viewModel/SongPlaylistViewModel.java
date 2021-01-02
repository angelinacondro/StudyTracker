package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.viewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.dao.SongPlaylistDao;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity.SongPlaylistData;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.repository.SongPlaylistRepository;

import java.util.List;

public class SongPlaylistViewModel extends ViewModel {
    private SongPlaylistRepository songPlaylistRepository;
    private SongPlaylistDao songPlaylistDao;
    private LiveData<List<SongPlaylistData>> allDatas;
//    private MediatorLiveData<List<SongPlaylistData>> mSectionLive = new MediatorLiveData<>();

    public void init (Context context){
        if (allDatas!= null){
            return;
        }
    }

    public LiveData<List<SongPlaylistData>> getDatas (){
        return allDatas;
    }

//    public SongPlaylistViewModel(@NonNull Application application) {
//        super(application);
//        songPlaylistRepository = new SongPlaylistRepository(application);
//        allDatas = songPlaylistRepository.getAllDatas();
//    }
//
//    public void insert (SongPlaylistData songPlaylistData){
//        songPlaylistRepository.insert(songPlaylistData);
//    }
//
//    public void update(SongPlaylistData songPlaylistData){
//        songPlaylistRepository.update(songPlaylistData);
//    }
//
//    public void delete(SongPlaylistData songPlaylistData){
//        songPlaylistRepository.delete(songPlaylistData);
//    }
//
//    public void deleteAllDatas(){
//        songPlaylistRepository.deleteAllDatas();
//    }
//
//    public LiveData<List<SongPlaylistData>> getAllDatas(){
//        return allDatas;
//    }

//    public LiveData<List<SongPlaylistData>> getAllDatasSecond(){
//        final LiveData<List<SongPlaylistData>> sections = songPlaylistRepository.getAllDatas();
//
//        mSectionLive.addSource(sections, new Observer<List<SongPlaylistData>>() {
//            @Override
//            public void onChanged(@Nullable List<SongPlaylistData> sectionList) {
//                if(sectionList == null || sectionList.isEmpty()) {
//                    // Fetch data from API
//                }else{
//                    mSectionLive.removeSource(sections);
//                    mSectionLive.setValue(sectionList);
//                }
//            }
//        });
//        return mSectionLive;
//    }
}
