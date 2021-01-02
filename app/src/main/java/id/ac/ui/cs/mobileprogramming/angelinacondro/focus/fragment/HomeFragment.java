package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.JNICallBackInterface;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.R;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.activity.AddSchedule;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.adapters.ListAdapter;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity.ScheduleData;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.helper.LocaleHelper;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.viewModel.ScheduleViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.paperdb.Paper;

public class HomeFragment extends Fragment implements ListAdapter.OnItemListener, JNICallBackInterface{
    FloatingActionButton add;
    RecyclerView recyclerView;
    ListAdapter listAdapter;
    TextView hello, schedule;
    ScheduleViewModel scheduleViewModel;
    private NativeLibrary nativeLib;
    FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Paper.init(getActivity());
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hello = view.findViewById(R.id.hellotxt);
        schedule = view.findViewById(R.id.yourschedule);
        nativeLib = new NativeLibrary();

        add = (FloatingActionButton) view.findViewById(R.id.add_button);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        listAdapter = new ListAdapter( this);
        recyclerView.setAdapter(listAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        scheduleViewModel.getAllDatas().observe(getActivity(), new Observer<List<ScheduleData>>() {
            @Override
            public void onChanged(List<ScheduleData> scheduleData) {
                List<ScheduleData> filteredList = new ArrayList<>();
                for(ScheduleData data: scheduleData){
                    if(data.getUser().equals(firebaseAuth.getCurrentUser().getUid())){
                        filteredList.add(data);
                    }
                }
                listAdapter.setData(filteredList);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addSchedule = new Intent(getActivity().getApplicationContext(), AddSchedule.class);
                startActivity(addSchedule);
            }
        });

        Paper.init(getActivity());
        String language = Paper.book().read("language");
        if(language == null){
            Paper.book().write("language", "en");
        }

        updateView((String)Paper.book().read("language"));
    }

    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(getActivity(), lang);
        Resources resources = context.getResources();
        hello.setText(resources.getString(R.string.hai));
        schedule.setText(resources.getString(R.string.schedule));
    }

    @Override
    public void onItemClick(int position) {

        Bundle bundle = new Bundle();
//        Intent intent = new Intent(getActivity(), DoTask.class);
        bundle.putString("judul", scheduleViewModel.getAllDatas().getValue().get(position).getTitle());

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

        long difference = countDifference(timeStart.getTime(), timeEnds.getTime());
        int days = (int) (difference / (1000*60*60*24));
        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
        hours = (hours < 0 ? -hours : hours);
        bundle.putLong("duration", difference);
        bundle.putString("mulai", timeStart.getHours() + ":" + timeStart.getMinutes());
        bundle.putString("selesai", timeEnds.getHours()  + ":" + timeEnds.getMinutes());

        DoTaskFragment fragment = new DoTaskFragment();
        fragment.setArguments(bundle);
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container,fragment).addToBackStack( "tag" );
        fr.commit();
    }
    public long countDifference(long timeStart, long timeEnd){
        long result = nativeLib.countDifference(timeStart, timeEnd);
        return result;
    }

    @Override
    public void callBackEvent(String data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });

    }
}
