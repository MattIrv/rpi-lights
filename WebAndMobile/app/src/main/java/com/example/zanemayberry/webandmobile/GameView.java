package com.example.zanemayberry.webandmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    private Paint yellowPaint;
    private Context context;


    public GameView(Context context, int width, int height, int rotation) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);
        gameThread = new GameThread(getHolder(), this, width, height, rotation);
        setFocusable(true);
        playerPaint = new Paint();
        bluePaint = new Paint();
        whitePaint = new Paint();
        greenPaint = new Paint();
        redPaint = new Paint();
        yellowPaint = new Paint();
        playerPaint.setARGB(255, 255, 255, 0);
        bluePaint.setARGB(255, 0, 0, 255);
        whitePaint.setARGB(255, 255, 255, 255);
        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setTextSize(20.0f);
        yellowPaint.setColor(Color.YELLOW);
        greenPaint.setARGB(255, 0, 255, 0);
        redPaint.setARGB(255, 255, 0, 0);
    }

    public void gameOver(GameState gameState) {
        Intent intent = new Intent(context, ScoreScreen.class);
        intent.putExtra("score", gameState.score);
        context.startActivity(intent);
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

    public void render(Canvas canvas, GameState gameState, long count) {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String url = "http://" + sharedPref.getString("ip_destination", "127.0.0.1") + "/rpi";
//        float widthDiv2 = (float)(getWidth() / 2.0);
//        float heightDiv2 = (float)(getHeight() / 2.0);
//        int redVal = (int)Math.max(255.0 * ((gameState.playerPosX - widthDiv2)/ widthDiv2), 50);
//        float whiteVal = (float)Math.max(-(gameState.playerPosX - widthDiv2)/ widthDiv2, 0.0);
//        int greenVal = (int)Math.max(255.0 * ((gameState.playerPosY - heightDiv2)/ heightDiv2), 50);
//        int blueVal = (int)Math.max(255.0 * (-(gameState.playerPosY - heightDiv2)/ heightDiv2), 50);
//        redVal += whiteVal * (255 - redVal);
//        greenVal += whiteVal * (255 - greenVal);
//        blueVal += whiteVal * (255 - blueVal);
//        playerPaint.setARGB(255, redVal, greenVal, blueVal);
        if (!gameState.isBlinked) {
            playerPaint.setColor(gameState.curDir.getColor());
        } else {
            playerPaint.setColor(gameState.nextDir.getColor());
        }
        canvas.drawColor(Color.BLACK);
        canvas.drawCircle(gameState.playerPosX, gameState.playerPosY, gameState.playerSize, playerPaint);
        for (EnemyState enemy : gameState.enemyStateList) {
            canvas.drawCircle(enemy.posX, enemy.posY, enemy.size, yellowPaint);
        }
        canvas.drawRect(0f, 0f, (float) getWidth(), gameState.edgeSize, bluePaint);
        canvas.drawRect(0f, (float) getHeight() - gameState.edgeSize, (float) getWidth(), (float) getHeight(), greenPaint);
        canvas.drawRect(0f, gameState.edgeSize, gameState.edgeSize, (float) getHeight() - gameState.edgeSize, whitePaint);
        canvas.drawRect((float)getWidth() - gameState.edgeSize, gameState.edgeSize, (float)getWidth(), (float)getHeight() - gameState.edgeSize, redPaint);
        canvas.drawText("Score: " + gameState.score, 100.0f, 100.0f, whitePaint);
        if (gameState.updateLights) {
            gameState.updateLights = false;
            final Direction curDir = gameState.curDir;
            final Direction nextDir = gameState.nextDir;
            int mode = 0;
            if (gameState.isChangingSoon) {
                mode++;
                if (gameState.isBlinked) {
                    mode++;
                }
            }
            System.out.println("Mode: " + mode);
            final int finalMode = mode;
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        MakeRequestTask.makeRequest(url, curDir, nextDir, finalMode);
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            });
            thread.start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
