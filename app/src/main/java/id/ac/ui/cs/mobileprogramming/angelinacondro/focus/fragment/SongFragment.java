package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.R;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.adapters.SongPlaylistAdapter;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity.SongPlaylistData;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.viewModel.SongPlaylistViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class SongFragment extends Fragment implements SongPlaylistAdapter.OnItemListener{
    RecyclerView recyclerView;
    SongPlaylistAdapter songPlaylistAdapter;
    SongPlaylistViewModel songPlaylistViewModel;
    ArrayList<SongPlaylistData> dataList;

    Uri audioUri;
    StorageReference storageReference;
    StorageTask mUploadsTask;
    DatabaseReference referenceSongs;
    MediaMetadataRetriever mediaMetadataRetriever;
    ProgressBar progressBar;
    String title, artist, data;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String userId;

    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static final int MY_PERMISSION_REQUEST = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);
        Button addButton = (Button) view.findViewById(R.id.btn_song);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAudioFiles(v);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaMetadataRetriever = new MediaMetadataRetriever();
        referenceSongs = FirebaseDatabase.getInstance().getReference().child("songs");
        storageReference = FirebaseStorage.getInstance().getReference().child("songs");
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView = view.findViewById(R.id.song_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        fAuth = FirebaseAuth.getInstance();

        retrieveSongs();

//        songPlaylistViewModel = ViewModelProviders.of(requireActivity()).get(SongPlaylistViewModel.class);
//        songPlaylistViewModel.getAllDatas().observe(getActivity(), new Observer<List<SongPlaylistData>>() {
//            @Override
//            public void onChanged(List<SongPlaylistData> songPlaylistData) {
//                songPlaylistAdapter.setData(songPlaylistData);
//            }
//        });
    }

    @Override
    public void onItemClick(int position) {

    }

    public void openAudioFiles (View v){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("audio/*");
            startActivityForResult(i, 101);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode){
            case MY_PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(getActivity(), "Permission Granted!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.setType("audio/*");
                        startActivityForResult(i, 101);
                    }
                } else {
                    Toast.makeText(getActivity(), "No Permission Granted!", Toast.LENGTH_SHORT).show();
                    Fragment SongFragment = new Fragment();
                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.fragment_container, SongFragment);
                    fr.commit();
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        View v = new View(getActivity());

        if(requestCode == 101 && resultCode == RESULT_OK && data.getData() != null){
            audioUri = data.getData();
            mediaMetadataRetriever.setDataSource(getActivity(), audioUri);
            title = getFileName(audioUri);
            artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            System.out.println(title + " " + artist);

        }
        uploadFileToFirebase(v);
    }

    private String getFileName(Uri uri){
        String result = null;
        if(uri.getScheme().equals("content")){
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if(result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if(cut != -1){
                result = result.substring(cut +1);
            }
        }
        return result;
    }
    
    public void uploadFileToFirebase (View v){
        if(mUploadsTask != null && mUploadsTask.isInProgress()){
            Toast.makeText(getActivity(), "Songs uploads in progress", Toast.LENGTH_SHORT).show();
        } else{
            uploadFiles();
        }
    }

    private void uploadFiles() {
        if(audioUri != null){
            Toast.makeText(getActivity(), "uploads please wait!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference storageRef = storageReference.child(System.currentTimeMillis() +
                    "."+getFileExtension(audioUri));
            mUploadsTask = storageRef.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               userId = fAuth.getCurrentUser().getUid();
                               System.out.println(title);
                               SongPlaylistData data = new SongPlaylistData(title, artist, uri.toString(), userId);
                               String uploadId = referenceSongs.push().getKey();
                               referenceSongs.child(uploadId).setValue(data);
                               progressBar.setVisibility(View.INVISIBLE);
                           }
                       });
                   }
               }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    progressBar.setProgress((int)progress);
                }
            });
        } else{
            Toast.makeText(getActivity(), "No file selected to uploads", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri audioUri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(audioUri));

    }

    private void retrieveSongs(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("songs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    SongPlaylistData song = ds.getValue(SongPlaylistData.class);
                    if(song.getUser().equals(fAuth.getCurrentUser().getUid())){
                        dataList.add(song);
                    }
                }
                songPlaylistAdapter = new SongPlaylistAdapter(getActivity(), dataList);
                recyclerView.setAdapter(songPlaylistAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void saveData(String title, String artist, String data){
//        SongPlaylistData dataLagu = new SongPlaylistData(title, artist, data);
////            dataLagu.setTitle(title);
////            dataLagu.setArtists(artist);
////            dataLagu.setData(data);
//        songPlaylistViewModel.insert(dataLagu);
////            songPlaylistAdapter.notifyDataSetChanged();
//    }
}
