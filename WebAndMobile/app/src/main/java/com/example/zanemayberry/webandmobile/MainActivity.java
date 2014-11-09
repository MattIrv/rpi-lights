package com.example.zanemayberry.webandmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends Activity implements SensorEventListener {

    private TextView gtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = new Intent(getBaseContext(), GameActivity.class);
        startActivity(intent);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SeekBar redbar = (SeekBar) findViewById(R.id.redbar);
        final SeekBar greenbar = (SeekBar) findViewById(R.id.greenbar);
        final SeekBar bluebar = (SeekBar) findViewById(R.id.bluebar);
        final SeekBar intensitybar = (SeekBar) findViewById(R.id.intensitybar);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SeekBar.OnSeekBarChangeListener l = new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar sb, int i, boolean b) {}
            public void onStartTrackingTouch(SeekBar sb) {}
            public void onStopTrackingTouch(SeekBar sb) {
                final String url = "http://" + sharedPref.getString("ip_destination", "127.0.0.1") + "/rpi";
                final int red = redbar.getProgress();
                final int green = greenbar.getProgress();
                final int blue = bluebar.getProgress();
                final double intensity = (double) intensitybar.getProgress() / 100.0;
                System.out.println("Submit Button clicked!");
                System.out.println("Sending request to: " + url);
                System.out.println("Red: " + red);
                System.out.println("Green: " + green);
                System.out.println("Blue: " + blue);
                System.out.println("Intensity: " + intensity);
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            MakeRequestTask.makeRequest(url, red, green, blue, intensity);
                        } catch (IOException e) {
                            System.out.println(e);
//                            showAlert();
                        }
                    }
                });
                thread.start();
            }
        };
        redbar.setOnSeekBarChangeListener(l);
        greenbar.setOnSeekBarChangeListener(l);
        bluebar.setOnSeekBarChangeListener(l);
        intensitybar.setOnSeekBarChangeListener(l);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gtv = (TextView) findViewById(R.id.gyroTV);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getBaseContext(), Prefs.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*private SensorManager mSensorManager;
    private Sensor mGyroSensor;
    private TextView gtv;
    private float[] rotationMatrix = {1, 0, 0, 0, 1, 0, 0, 0, 1};

    public void initGyro() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, mGyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        gtv = (TextView) findViewById(R.id.gyroTV);
    }

    // Create a constant to convert nanoseconds to seconds.
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;

    @Override
    public void onSensorChanged(SensorEvent event) {
        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            // Axis of the rotation sample, not normalized yet.
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            // Calculate the angular speed of the sample
            float omegaMagnitude = (float)Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

            // Normalize the rotation vector if it's big enough to get the axis
            // (that is, EPSILON should represent your maximum allowable margin of error)
            if (omegaMagnitude > 0.1) {
                axisX /= omegaMagnitude;
                axisY /= omegaMagnitude;
                axisZ /= omegaMagnitude;
            }

            // Integrate around this axis with the angular speed by the timestep
            // in order to get a delta rotation from this sample over the timestep
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            float thetaOverTwo = omegaMagnitude * dT / 2.0f;
            float sinThetaOverTwo = (float)Math.sin(thetaOverTwo);
            float cosThetaOverTwo = (float)Math.cos(thetaOverTwo);
            deltaRotationVector[0] = sinThetaOverTwo * axisX;
            deltaRotationVector[1] = sinThetaOverTwo * axisY;
            deltaRotationVector[2] = sinThetaOverTwo * axisZ;
            deltaRotationVector[3] = cosThetaOverTwo;
        }
        timestamp = event.timestamp;
        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
        // User code should concatenate the delta rotation we computed with the current rotation
        // in order to get the updated rotation.
        // rotationCurrent = rotationCurrent * deltaRotationMatrix;
        rotationMatrix = naivMatrixMultiply(deltaRotationMatrix, rotationMatrix);
        gtv.setText("");
        for (float entry: rotationMatrix) {
            gtv.append(entry + ", ");
        }
        mSensorManager.sensor
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something if sensor accuracy changes.
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mGyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // important to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);
    }*/

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
    protected void onResume() {
        super.onResume();
        mLastAccelSet = false;
        mLastMagSet = false;
        mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMag, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

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
            SensorManager.getOrientation(mR, mOrientation);
            gtv.setText(String.format("Azimuth: %f, Pitch: %f, Roll: %f",
                    mOrientation[0], mOrientation[1], mOrientation[2]));
        }
    }

//    public void showAlert() {
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        String message = "Failed to send to http://" + sharedPref.getString("ip_destination", "127.0.0.1") + "/rpi";
//        System.out.println(message);
//        new AlertDialog.Builder(this)
//                .setTitle("Error")
//                .setMessage(message)
//                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface d, int i) {
//                        d.dismiss();
//                    }})
//                .show();
//    }

}
