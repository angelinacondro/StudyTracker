package id.ac.ui.cs.mobileprogramming.angelinacondro.focus;

import android.app.Application;
import android.content.Context;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.helper.LocaleHelper;

public class MainApplication extends Application {
    protected void attachBaseContext (Context base){
        super.attachBaseContext(LocaleHelper.onAttach(base,"en"));
    }
}
