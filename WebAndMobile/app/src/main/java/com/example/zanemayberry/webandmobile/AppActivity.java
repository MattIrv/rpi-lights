package com.example.zanemayberry.webandmobile;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

public class AppActivity extends Activity {

    Button button1;
    Button button2;
    Button button3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        addListenerOnButton1();
        addListenerOnButton2();
        addListenerOnButton3();
    }
    //button 1 = BEGIN
    public void addListenerOnButton1() {

        final Context context = this;

        button1 = (Button) findViewById(R.id.button1);

        button1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, GameActivity.class);
                startActivity(intent);

            }

        });

    }

    //button 2 = INSTRUCTIONS
    public void addListenerOnButton2() {

        final Context context = this;

        button2 = (Button) findViewById(R.id.button2);

        button2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, App2Activity.class);
                startActivity(intent);

            }

        });

    }

    //button 3 = SETTINGS
    public void addListenerOnButton3() {

        final Context context = this;

        button3 = (Button) findViewById(R.id.button3);

        button3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, Prefs.class);
                startActivity(intent);

            }

        });

    }
}