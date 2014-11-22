package com.example.zanemayberry.webandmobile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;


public class ScoreScreen extends Activity {

    static int highScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);
        final int score = getIntent().getExtras().getInt("score");
        final TextView tvScore = (TextView) findViewById(R.id.tv_score);
        final TextView tvHighScore = (TextView) findViewById(R.id.tv_high_score);
        final Button shareButton = (Button) findViewById(R.id.btn_fb);
        final Button playAgain = (Button) findViewById(R.id.btn_play_again);
        final RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                startActivity(intent);
            }
        });
        tvScore.setText(score + "");
        Drawable stars = rb.getProgressDrawable();
        stars.setColorFilter(Color.argb(255, 255, 215, 0), PorterDuff.Mode.SRC_ATOP);
        if (score > 5000) {
            rb.setNumStars(5);
        }
        else if (score > 4000) {
            rb.setNumStars(4);
        }
        else if (score > 3000) {
            rb.setNumStars(3);
        }
        else if (score > 2000) {
            rb.setNumStars(2);
        }
        else if (score > 1000) {
            rb.setNumStars(1);
        }
        else {
            rb.setNumStars(0);
        }
        if (score > highScore) {
            highScore = score;
            tvHighScore.setText("New high score!");
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareFacebookHighScore(score);
                }
            });
        }
        else {
            tvHighScore.setText("High score: " + highScore);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareFacebookScore(score);
                }
            });
        }
    }

    public void shareFacebookHighScore(int score) {

    }

    public void shareFacebookScore(int score) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.score_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
