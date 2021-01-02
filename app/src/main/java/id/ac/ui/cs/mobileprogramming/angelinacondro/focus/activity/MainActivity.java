package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.R;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.fragment.ScheduleFragment;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.fragment.SongFragment;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.helper.LocaleHelper;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.fragment.HomeFragment;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.viewModel.SongPlaylistViewModel;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SongPlaylistViewModel songPlaylistViewModel;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

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
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initViewPager();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
//        songPlaylistViewModel = ViewModelProviders.of(this).get(SongPlaylistViewModel.class);
//        songPlaylistViewModel.getAllDatas().observe(this, new Observer<List<SongPlaylistData>>() {
//            @Override
//            public void onChanged(List<SongPlaylistData> songPlaylistData) {
//            }
//        });

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                    HomeFragment()).commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
//        hello.setText(resources.getString(R.string.hai));
//        schedule.setText(resources.getString(R.string.schedule));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                        HomeFragment()).addToBackStack( "1" ).commit();
                break;
            case R.id.nav_schedule:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                        ScheduleFragment()).addToBackStack( "2" ).commit();
                break;
            case R.id.nav_song:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                        SongFragment()).addToBackStack( "3" ).commit();
                break;
            case R.id.nav_logout:
                logout();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
//
    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }



}