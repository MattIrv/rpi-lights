package com.example.student.mkyongtutorial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

public class App2Activity extends Activity {

    Button button4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        addListenerOnButton4();
    }

    public void addListenerOnButton4() {

        final Context context = this;

        button4 = (Button) findViewById(R.id.button4);

        button4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, AppActivity.class);
                startActivity(intent);

            }

        });

    }

}