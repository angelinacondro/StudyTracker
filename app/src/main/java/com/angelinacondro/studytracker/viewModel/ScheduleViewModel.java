package com.angelinacondro.studytracker.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.angelinacondro.studytracker.db.entity.ScheduleData;
import com.angelinacondro.studytracker.repository.ScheduleRepository;

import java.util.List;

public class ScheduleViewModel extends AndroidViewModel {
    private ScheduleRepository scheduleRepository;
    private LiveData<List<ScheduleData>> allDatas;

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        scheduleRepository = new ScheduleRepository(application);
        allDatas = scheduleRepository.getAllDatas();
    }

    public void insert (ScheduleData scheduleData){
        scheduleRepository.insert(scheduleData);
    }

    public void update(ScheduleData scheduleData){
        scheduleRepository.update(scheduleData);
    }

    public void delete(ScheduleData scheduleData){
        scheduleRepository.delete(scheduleData);
    }

    public void deleteAllDatas(){
        scheduleRepository.deleteAllDatas();
    }

    public LiveData<List<ScheduleData>> getAllDatas(){
        return allDatas;
    }
}
