package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.R;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity.SongPlaylistData;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.viewModel.SongPlaylistViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static id.ac.ui.cs.mobileprogramming.angelinacondro.focus.helper.NotificationHelper.channelID;

public class DoTaskFragment extends Fragment{
    private TextView Waktu, Judul, StartTime, EndTime, songTextLabel;
    private Button Start, Stop, Choose, btn_next, btn_previous, btn_pause;
    private CountDownTimer mCountDownTimer;
    private long min;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;
    private SeekBar songSeekBar;
    SongPlaylistViewModel songPlaylistViewModel;

    private Boolean wifiConnected, mobileConnected;

    static MediaPlayer mediaPlayer;
    int position = 0;
    String sname;

    List<SongPlaylistData> mySongs;
    Thread updateSeekBar;
    FirebaseAuth fAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_do_task, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_next = view.findViewById(R.id.next);
        btn_previous = view.findViewById(R.id.prev);
        btn_pause = view.findViewById(R.id.pause);
        songTextLabel = view.findViewById(R.id.song_name);
        songSeekBar = view.findViewById(R.id.seekBar);
        fAuth = FirebaseAuth.getInstance();


        Waktu = view.findViewById(R.id.timer);
        Judul = view.findViewById(R.id.judul);
        StartTime = view.findViewById(R.id.time);
        Start = view.findViewById(R.id.start);
        Stop = view.findViewById(R.id.stop);

        checkNetworkConnection();

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

        Bundle extras = this.getArguments();

        if(extras == null){
            Judul.setText("");
        } else {
            Judul.setText(extras.getString("judul"));
            StartTime.setText(extras.getString("mulai") + "-" + extras.getString("selesai"));
            min = extras.getLong("duration");
        }

        mTimeLeftInMillis = min;
//        timerClass = new TimerClass(60000 * min, 1000);
        String time = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(mTimeLeftInMillis),
                TimeUnit.MILLISECONDS.toMinutes(min) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mTimeLeftInMillis)),
                TimeUnit.MILLISECONDS.toSeconds(min) -
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
                doNotify();
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
        mTimeLeftInMillis = min;
        updateCountDownText();
        updateButtons();
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("millisLeft", mTimeLeftInMillis);
        outState.putBoolean("timerRunning", mTimerRunning);
        outState.putLong("endTime", mEndTime);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {
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

    public void doNotify(){
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(getActivity().getApplicationContext(), channelID);
        nb.setSound(sound);
        nb.setSmallIcon(R.drawable.icon);
        nb.setContentTitle("Done!");
        nb.setContentText("You have finished a task!");

        NotificationManager nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(100, nb.build());
    }

    private void retrieveSongs(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("songs");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mySongs = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    SongPlaylistData song = ds.getValue(SongPlaylistData.class);
                    if(song.getUser().equals(fAuth.getCurrentUser().getUid())){
                        mySongs.add(song);
                    }

                }
                initPlayer();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initPlayer(){

        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        sname = mySongs.get(position).getTitle();
        songTextLabel.setText(sname);
        songTextLabel.setSelected(true);

        Uri u = Uri.parse(mySongs.get(position).getData());

        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), u);
        mediaPlayer.start();
        mediaPlayer.pause();
        mediaPlayer.setLooping(true);
        songSeekBar.setMax(mediaPlayer.getDuration());

        updateSeekBar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;
                while (currentPosition<totalDuration){
                    try{
                        sleep(500);
                        currentPosition= mediaPlayer.getCurrentPosition();
                        songSeekBar.setProgress(currentPosition);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        updateSeekBar.start();

        songSeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);
        songSeekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);

        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songSeekBar.setMax(mediaPlayer.getDuration());
                if(mediaPlayer.isPlaying()){
                    btn_pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                    mediaPlayer.pause();
                } else {
                    btn_pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                position = ((position + 1) % mySongs.size());

                Uri u = Uri.parse(mySongs.get(position).getData());

                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), u);
                sname = mySongs.get(position).getTitle();
                songTextLabel.setText(sname);
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
                songSeekBar.setProgress(0);
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                position = ((position - 1)<0)? (mySongs.size()-1):(position-1);

                Uri u = Uri.parse(mySongs.get(position).getData());
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), u);
                sname = mySongs.get(position).getTitle();
                songTextLabel.setText(sname);
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });


    }

    private void checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean connected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if(!connected){
            songTextLabel.setText("Please connect to internet!");
        } else {
            songTextLabel.setText("Loading...");
            retrieveSongs();
        }
    }

    @Override
    public void onDetach(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        super.onDetach();
    }


}
