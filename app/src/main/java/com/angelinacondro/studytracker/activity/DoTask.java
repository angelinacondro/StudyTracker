package com.angelinacondro.studytracker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.angelinacondro.studytracker.R;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DoTask extends AppCompatActivity{

//    private static final long START_TIME_IN_MILLIS = 600000;

    private TextView Waktu, Judul, StartTime, EndTime;
    private Button Start, Stop;
    private CountDownTimer mCountDownTimer;
    private int min;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_task);

        Waktu = findViewById(R.id.timer);
        Judul = findViewById(R.id.judul);
        StartTime = findViewById(R.id.time);
        Start = findViewById(R.id.start);
        Stop = findViewById(R.id.stop);
        Start.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mTimerRunning){
                    pauseTimer();
                } else{
                    startTimer();
                }
            }
        });

        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        updateCountDownText();
        if(mTimerRunning == false){
            Stop.setVisibility(View.INVISIBLE);
        }

        Bundle extras = getIntent().getExtras();

        if(extras == null){
            Judul.setText("");
        } else {
            Judul.setText(extras.getString("judul"));
            StartTime.setText(extras.getString("mulai") + "-" + extras.getString("selesai"));
            min = extras.getInt("duration");

        }

        mTimeLeftInMillis = min * 60000;
//        timerClass = new TimerClass(60000 * min, 1000);
        String time = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(mTimeLeftInMillis),
                TimeUnit.MILLISECONDS.toMinutes(60000 * min) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mTimeLeftInMillis)),
                TimeUnit.MILLISECONDS.toSeconds(60000 * min) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mTimeLeftInMillis)));
        Waktu.setText(time);
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateButtons();
            }
        }.start();

        mTimerRunning = true;
        updateButtons();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateButtons();
    }

    private void resetTimer() {
        mTimeLeftInMillis = min * 60000;
        updateCountDownText();
        updateButtons();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        Waktu.setText(timeLeftFormatted);
    }

    private void updateButtons() {
        if (mTimerRunning) {
            Stop.setVisibility(View.INVISIBLE);
            Start.setText("Pause");
        } else {
            Start.setText("Start");

            if (mTimeLeftInMillis < 1000) {
                Start.setVisibility(View.INVISIBLE);
            } else {
                Start.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < (min * 60000)) {
                Stop.setVisibility(View.VISIBLE);
            } else {
                Stop.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("millisLeft", mTimeLeftInMillis);
        outState.putBoolean("timerRunning", mTimerRunning);
        outState.putLong("endTime", mEndTime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mTimeLeftInMillis = savedInstanceState.getLong("millisLeft");
        mTimerRunning = savedInstanceState.getBoolean("timerRunning");
        updateCountDownText();
        updateButtons();

        if (mTimerRunning) {
            mEndTime = savedInstanceState.getLong("endTime");
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            startTimer();
        }
    }
}