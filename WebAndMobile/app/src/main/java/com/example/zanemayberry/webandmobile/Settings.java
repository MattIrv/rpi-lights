package com.example.zanemayberry.webandmobile;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

public class Settings extends Activity {

    Button button5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        addListenerOnButton5();
    }

    public void addListenerOnButton5() {

        final Context context = this;

        button5 = (Button) findViewById(R.id.button5);

        button5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, AppActivity.class);
                startActivity(intent);

            }

        });

    }

}