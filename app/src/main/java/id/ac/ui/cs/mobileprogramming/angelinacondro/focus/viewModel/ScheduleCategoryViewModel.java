package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity.ScheduleCategoryData;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.repository.ScheduleCategoryRepository;

import java.util.List;

public class ScheduleCategoryViewModel extends AndroidViewModel {
    private ScheduleCategoryRepository dataRepository;
    private LiveData<List<ScheduleCategoryData>> allDatas;

    public ScheduleCategoryViewModel(@NonNull Application application) {
        super(application);
        dataRepository = new ScheduleCategoryRepository(application);
        allDatas = dataRepository.getAllDatas();
    }

    public void insert (ScheduleCategoryData scheduleData){
        dataRepository.insert(scheduleData);
    }

    public void update(ScheduleCategoryData scheduleCategoryData){
        dataRepository.update(scheduleCategoryData);
    }

    public void delete(ScheduleCategoryData scheduleCategoryData){
        dataRepository.delete(scheduleCategoryData);
    }

    public void deleteAllDatas(){
        dataRepository.deleteAllDatas();
    }

    public LiveData<List<ScheduleCategoryData>> getAllDatas(){
        return allDatas;
    }
}
