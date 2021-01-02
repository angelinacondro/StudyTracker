package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.fragment;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.JNICallBackInterface;

class NativeLibrary {

    JNICallBackInterface callBackInterface;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("duration");
    }

    public NativeLibrary() {
//        this.callBackInterface = callBackInterface;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    public native long countDifference(long timeStarts, long timeEnds);
}
