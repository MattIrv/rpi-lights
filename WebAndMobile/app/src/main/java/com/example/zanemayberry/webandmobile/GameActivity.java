package com.example.zanemayberry.webandmobile;

import android.annotation.TargetApi;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;import java.lang.Override;

public class GameActivity extends Activity implements SensorEventListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    GameView gv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Creating activity!");
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        gv = new GameView(this, width, height);
        setContentView(gv);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
    }

    boolean paused = false;

    @Override
    protected void onPause() {
        super.onPause();
        if (gv.gameThread != null) {
            gv.gameThread.setRunning(false);
            paused = true;
        }
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            gv.gameThread.setRunning(true);
            paused = false;
        }
        mLastAccelSet = false;
        mLastMagSet = false;
        mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMag, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
    Code to use the accelerometer and magnet sensor to calculate rotation.
    Adapted from http://stackoverflow.com/questions/4675750/lock-screen-orientation-android
 */
    private SensorManager mSensorManager;
    private Sensor mAccel;
    private Sensor mMag;

    private float[] mLastAccel = new float[3];
    private float[] mLastMag = new float[3];
    private boolean mLastAccelSet = false;
    private boolean mLastMagSet = false;

    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];

    @Override
    public void onAccuracyChanged(Sensor sensor, int acc) {

    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        if (e.sensor == mAccel) {
            System.arraycopy(e.values, 0, mLastAccel, 0, e.values.length);
            mLastAccelSet = true;
        }
        else if (e.sensor == mMag) {
            System.arraycopy(e.values, 0, mLastMag, 0, e.values.length);
            mLastMagSet = true;
        }
        if (mLastAccelSet && mLastMagSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccel, mLastMag);
            SensorManager.getOrientation(mR, mOrientation); // Azimuth, Pitch, Roll
            if (gv.gameThread != null)
                gv.gameThread.updateOrientation(mOrientation);
            //gtv.setText(String.format("Azimuth: %f, Pitch: %f, Roll: %f",
            //        mOrientation[0], mOrientation[1], mOrientation[2]));
        }
    }

}
