package com.example.zanemayberry.webandmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import java.io.IOException;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
