package com.angelinacondro.studytracker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.angelinacondro.studytracker.db.entity.ScheduleData;
import com.angelinacondro.studytracker.R;
import com.angelinacondro.studytracker.adapters.ListAdapter;
import com.angelinacondro.studytracker.db.RoomDB;
import com.angelinacondro.studytracker.helper.LocaleHelper;
import com.angelinacondro.studytracker.viewModel.ScheduleViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements ListAdapter.OnItemListener {

    FloatingActionButton add;
    RecyclerView recyclerView;
    ListAdapter listAdapter;
    TextView hello, schedule;
    ScheduleViewModel scheduleViewModel;

    @Override
    protected void attachBaseContext (Context newBase){
        super.attachBaseContext(LocaleHelper.onAttach(newBase,"en"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Paper.init(this);
        if(item.getItemId() == R.id.language_en){
            Paper.book().write("language", "en");
            updateView((String)Paper.book().read("language"));
        } else if(item.getItemId() == R.id.language_id){
            Paper.book().write("language", "id");
            updateView((String)Paper.book().read("language"));
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = (FloatingActionButton) findViewById(R.id.add_button);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        listAdapter = new ListAdapter( this);
        recyclerView.setAdapter(listAdapter);

        hello = findViewById(R.id.hellotxt);
        schedule = findViewById(R.id.yourschedule);

        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        scheduleViewModel.getAllDatas().observe(this, new Observer<List<ScheduleData>>() {
            @Override
            public void onChanged(List<ScheduleData> scheduleData) {
                listAdapter.setData(scheduleData);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addSchedule = new Intent(getApplicationContext(), AddSchedule.class);
                startActivity(addSchedule);
            }
        });

        Paper.init(this);
        String language = Paper.book().read("language");
        if(language == null){
            Paper.book().write("language", "en");
        }

        updateView((String)Paper.book().read("language"));
    }

    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(this, lang);
        Resources resources = context.getResources();
        hello.setText(resources.getString(R.string.hai));
        schedule.setText(resources.getString(R.string.schedule));
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, DoTask.class);
        intent.putExtra("judul", scheduleViewModel.getAllDatas().getValue().get(position).getTitle());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");


        Date timeStart = null;
        Date timeEnds = null;

        try {
            timeStart = simpleDateFormat.parse(scheduleViewModel.getAllDatas().getValue().get(position).getTimeStarts());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            timeEnds = simpleDateFormat.parse(scheduleViewModel.getAllDatas().getValue().get(position).getTimeEnds());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = timeEnds.getTime() - timeStart.getTime();
        int days = (int) (difference / (1000*60*60*24));
        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
        hours = (hours < 0 ? -hours : hours);
        intent.putExtra("duration", min);
        intent.putExtra("mulai", timeStart.getHours() + ":" + timeStart.getMinutes());
        intent.putExtra("selesai", timeEnds.getHours()  + ":" + timeEnds.getMinutes());
        startActivity(intent);
    }
}