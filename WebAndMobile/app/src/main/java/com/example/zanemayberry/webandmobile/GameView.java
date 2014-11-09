package com.example.zanemayberry.webandmobile;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;
import java.lang.InterruptedException;import java.lang.Override;import java.lang.System;


/**
 * TODO: document your custom view class.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public GameThread gameThread;
    private Paint playerPaint;
    private Paint bluePaint;
    private Paint whitePaint;
    private Paint greenPaint;
    private Paint redPaint;


    public GameView(Context context, int width, int height) {
        super(context);
        getHolder().addCallback(this);
        gameThread = new GameThread(getHolder(), this, width, height);
        setFocusable(true);
        playerPaint = new Paint();
        bluePaint = new Paint();
        whitePaint = new Paint();
        greenPaint = new Paint();
        redPaint = new Paint();
        playerPaint.setARGB(255, 255, 255, 0);
        bluePaint.setARGB(255, 0, 0, 255);
        whitePaint.setARGB(255, 255, 255, 255);
        greenPaint.setARGB(255, 0, 255, 0);
        redPaint.setARGB(255, 255, 0, 0);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }
    @Override
   	public void surfaceCreated(SurfaceHolder holder) {
        gameThread.setRunning(true);
        try {
            gameThread.start();
        }
        catch (IllegalThreadStateException e) {
            // Thread was already started nbd
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getY() > getHeight() - 50) {
                gameThread.setRunning(false);
                ((Activity)getContext()).finish();
            } else {
                System.out.println("x: " + event.getX() + "; y: " + event.getY());
            }
        }
        return super.onTouchEvent(event);
    }

    public void render(Canvas canvas, GameState gameState) {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String url = "http://" + sharedPref.getString("ip_destination", "127.0.0.1") + "/rpi";
        float widthDiv2 = (float)(getWidth() / 2.0);
        float heightDiv2 = (float)(getHeight() / 2.0);
        int redVal = (int)Math.max(255.0 * ((gameState.playerPosX - widthDiv2)/ widthDiv2), 0.0);
        float whiteVal = (float)Math.max(-(gameState.playerPosX - widthDiv2)/ widthDiv2, 0.0);
        int greenVal = (int)Math.max(255.0 * ((gameState.playerPosX - heightDiv2)/ heightDiv2), 0.0);
        int blueVal = (int)Math.max(255.0 * (-(gameState.playerPosX - heightDiv2)/ heightDiv2), 0.0);
        redVal += whiteVal * (255 - redVal);
        greenVal += whiteVal * (255 - greenVal);
        blueVal += whiteVal * (255 - blueVal);
        playerPaint.setARGB(255, redVal, greenVal, blueVal);
        canvas.drawColor(Color.BLACK);
        canvas.drawCircle(gameState.playerPosX, gameState.playerPosY, gameState.playerSize, playerPaint);
        canvas.drawRect(50f, 0f, (float) getWidth() - 50f, 50f, bluePaint);
        canvas.drawRect(50f, (float) getHeight() - 50f, (float) getWidth() - 50f, (float) getHeight(), greenPaint);
        canvas.drawRect(0f, 50f, 50f, (float) getHeight() - 50f, whitePaint);
        canvas.drawRect((float)getWidth() - 50f, 50f, (float)getWidth(), (float)getHeight() - 50f, redPaint);
//        final int red = redVal;
//        final int green = greenVal;
//        final int blue = blueVal;
//        Thread thread = new Thread(new Runnable() {
//            public void run() {
//                try {
//                    MakeRequestTask.makeRequest(url, red, green, blue, 1.0);
//                } catch (IOException e) {
//                    System.out.println(e);
////                            showAlert();
//                }
//            }
//        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
