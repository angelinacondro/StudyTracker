package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.activity;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.openGL.OpenGLRenderer;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.openGL.OpenGLView;

public class SplashScreen extends Activity {

    private OpenGLView openGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        GLSurfaceView view = new GLSurfaceView(this);
        view.setRenderer(new OpenGLRenderer());
        setContentView(view);
//        setContentView(R.layout.splash);
//        openGLView = (OpenGLView) findViewById(R.id.openGLView);

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreen.this, Register.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onResume(){
        super.onResume();
//        openGLView.onResume();
    }
    @Override
    protected void onPause() { // TODO Auto-generated method stub
        super.onPause();
//        openGLView.onPause();
        finish();
    }
}
