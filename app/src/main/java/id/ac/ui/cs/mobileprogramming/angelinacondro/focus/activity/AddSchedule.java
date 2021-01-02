package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.adapters.ScheduleCategoryAdapter;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity.ScheduleCategoryData;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity.ScheduleData;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.fragment.CategoryDialog;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.receiver.AlertReceiver;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.R;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.adapters.MainAdapter;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.viewModel.ScheduleCategoryViewModel;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.viewModel.ScheduleViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddSchedule extends AppCompatActivity implements CategoryDialog.ExampleDialogListener {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter, timeFormatter;
    MaterialEditText title, btDatePicker, timeEnds, timeStarts;
    Spinner category;
    ScheduleViewModel scheduleViewModel;
    ScheduleCategoryViewModel scheduleCategoryViewModel;
    Button bsimpan, balarm, bcategory;

    List<ScheduleData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    MainAdapter mainAdapter;
    ScheduleCategoryAdapter scheduleCategoryAdapter;
    ArrayList<ScheduleCategoryData> categoryList = new ArrayList<>();
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule2);

        initList();

        // inisialisasi
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("hh:mm");
        btDatePicker = findViewById(R.id.date);
        timeStarts = findViewById(R.id.time_start);
        timeEnds = findViewById(R.id.time_end);
        bsimpan = findViewById(R.id.simpan_button);
        balarm = findViewById(R.id.alarm_button);
        bcategory = findViewById(R.id.add_category);
        title = findViewById(R.id.title);
        category = findViewById(R.id.category);
        fAuth = FirebaseAuth.getInstance();

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainAdapter = new MainAdapter(AddSchedule.this, dataList);

        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        scheduleViewModel.getAllDatas().observe(this, new Observer<List<ScheduleData>>() {
            @Override
            public void onChanged(List<ScheduleData> scheduleData) {
                mainAdapter.setData(scheduleData);
            }
        });


        scheduleCategoryAdapter = new ScheduleCategoryAdapter(this, categoryList);
        category.setAdapter(scheduleCategoryAdapter);

        scheduleCategoryViewModel = ViewModelProviders.of(this).get(ScheduleCategoryViewModel.class);
        scheduleCategoryViewModel.getAllDatas().observe(this, new Observer<List<ScheduleCategoryData>>(){
            @Override
            public void onChanged(@Nullable final List<ScheduleCategoryData> scheduleData) {
                for(ScheduleCategoryData categories : scheduleData){
                    categoryList.add(categories);
                }
                scheduleCategoryAdapter.notifyDataSetChanged();
            }
        });

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               ScheduleCategoryData clickedItem = (ScheduleCategoryData) parent.getItemAtPosition(position);
               String clickedCountryName = clickedItem.getCategory();
               Toast.makeText(AddSchedule.this, clickedCountryName + " selected", Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {
           }
        });



        bcategory.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                openCategoryDialog();
            }
        });


        balarm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String sTitle = title.getText().toString().trim();
                String sDate = btDatePicker.getText().toString().trim();
                String sTimeStarts = timeStarts.getText().toString().trim();

                Date theDate = null;
                Date theTimeStarts = null;
                try {
                    theDate = dateFormatter.parse(sDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    theTimeStarts = timeFormatter.parse(sTimeStarts);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Intent openNewAlarm = new Intent(AlarmClock.ACTION_SET_ALARM);
                openNewAlarm.putExtra(AlarmClock.EXTRA_HOUR, theTimeStarts.getHours());
                openNewAlarm.putExtra(AlarmClock.EXTRA_MINUTES, theTimeStarts.getMinutes());
                openNewAlarm.putExtra(AlarmClock.EXTRA_DAYS, theDate.getDay());
                openNewAlarm.putExtra(AlarmClock.EXTRA_MESSAGE, sTitle);

                startActivity(openNewAlarm);
            }
        });

        bsimpan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
//
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        btDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        timeStarts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeStartsDialog();
            }
        });
        timeEnds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeEndsDialog();
            }
        });
    }

    private void openCategoryDialog() {
        CategoryDialog dialog = new CategoryDialog();
        dialog.show(getSupportFragmentManager(), "Dialog");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void saveData(){
        String sTitle = title.getText().toString().trim();
        String sDate = btDatePicker.getText().toString().trim();
        String sTimeStarts = timeStarts.getText().toString().trim();
        String sTimeEnds = timeEnds.getText().toString().trim();
        String sCategory = category.getSelectedItem().toString().trim();

        Date theDate = null;
        Date theTimeStarts = null;
        try {
            theDate = dateFormatter.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            theTimeStarts = timeFormatter.parse(sTimeStarts);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.set(theDate.getYear(), theDate.getMonth(),theDate.getDay(), theTimeStarts.getHours(), theTimeStarts.getMinutes());


        if(!sTitle.equals("") && !sDate.equals("") && !sTimeStarts.equals("") && !sTimeEnds.equals("") && !sCategory.equals("")) {
            ScheduleData data = new ScheduleData();
            data.setTitle(sTitle);
            data.setDate(theDate);
            data.setTimeStarts(sTimeStarts);
            data.setTimeEnds(sTimeEnds);
            data.setCategory(sCategory);
            data.setUser(fAuth.getCurrentUser().getUid());
            scheduleViewModel.insert(data);
            mainAdapter.notifyDataSetChanged();
            startAlarm(cal);
            startActivity(new Intent(AddSchedule.this, MainActivity.class));
        }
    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                btDatePicker.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showTimeStartsDialog(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddSchedule.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeStarts.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void showTimeEndsDialog(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddSchedule.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeEnds.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        boolean alarmUp = (PendingIntent.getBroadcast(this, 0,
                new Intent("com.my.package.MY_UNIQUE_ACTION"),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp) {
            Log.d("myTag", "Alarm is already active");
        }
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    private void initList() {
        categoryList = new ArrayList<>();
        categoryList.add(new ScheduleCategoryData("Study"));
    }

    @Override
    public void applyTexts(String category) {

        scheduleCategoryViewModel = ViewModelProviders.of(this).get(ScheduleCategoryViewModel.class);
        scheduleCategoryViewModel.insert(new ScheduleCategoryData((category)));
    }
}