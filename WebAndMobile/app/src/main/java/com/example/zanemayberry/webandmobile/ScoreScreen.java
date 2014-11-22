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

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;

import java.util.Arrays;


public class ScoreScreen extends Activity {

    static int highScore = 0;
    static boolean loggedIn = false;
    static boolean sharingHighScore = false;
    static boolean sharingScore = false;
    static int score = 0;

    LoginButton lb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        score = getIntent().getExtras().getInt("score");
        final TextView tvScore = (TextView) findViewById(R.id.tv_score);
        final TextView tvHighScore = (TextView) findViewById(R.id.tv_high_score);
        final Button shareButton = (Button) findViewById(R.id.btn_fb);
        final Button playAgain = (Button) findViewById(R.id.btn_play_again);
        final RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);
        lb = (LoginButton) findViewById(R.id.authButton);
        lb.setPublishPermissions(Arrays.asList("publish_actions"));
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
        if (score > 1000) {
            rb.setVisibility(View.VISIBLE);
        }
        else {
            rb.setVisibility(View.INVISIBLE);
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
        if (!loggedIn) {
            sharingHighScore = true;
            lb.callOnClick();
        }
        else {
            sharingHighScore = false;
            if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                    FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
                // Publish the post using the Share Dialog
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                        .setApplicationName("SlideBall")
                        .setDescription("I just set a new high score of " + score + " on SlideBall!")
                        .setCaption("Click to download SlideBall for your Android device.")
                        .setLink("http://people.virginia.edu/~mji7wb/app-release-ms5.apk")
                        .build();
                uiHelper.trackPendingDialogCall(shareDialog.present());

            } else {
                // Fallback. For example, publish the post using the Feed Dialog
            }
        }
    }

    public void shareFacebookScore(int score) {
        if (!loggedIn) {
            sharingScore = true;
            lb.callOnClick();
        }
        else {
            sharingScore = false;
            if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                    FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
                // Publish the post using the Share Dialog
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                        .setApplicationName("SlideBall")
                        .setDescription("I just set a new high score of " + score + " on SlideBall!")
                        .setCaption("Click to download SlideBall for your Android device.")
                        .setLink("http://people.virginia.edu/~mji7wb/app-release-ms5.apk")
                        .build();
                uiHelper.trackPendingDialogCall(shareDialog.present());

            } else {
                // Fallback. For example, publish the post using the Feed Dialog
            }
        }
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

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            loggedIn = true;
            if (sharingHighScore) {
                shareFacebookHighScore(highScore);
            }
            if (sharingScore) {
                shareFacebookScore(score);
            }
        } else if (state.isClosed()) {
            loggedIn = false;
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private UiLifecycleHelper uiHelper;

    @Override
    public void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

}
