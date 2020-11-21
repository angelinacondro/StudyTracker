package com.angelinacondro.studytracker;

import android.app.Application;
import android.content.Context;

import com.angelinacondro.studytracker.helper.LocaleHelper;

public class MainApplication extends Application {
    protected void attachBaseContext (Context base){
        super.attachBaseContext(LocaleHelper.onAttach(base,"en"));
    }
}
