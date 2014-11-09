package com.example.zanemayberry.webandmobile;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;import java.lang.InterruptedException;import java.lang.Override;import java.lang.System;


/**
 * TODO: document your custom view class.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public GameThread gameThread;
    private Paint playerPaint;

    public GameView(Context context, int width, int height) {
        super(context);
        getHolder().addCallback(this);
        gameThread = new GameThread(getHolder(), this, width, height);
        setFocusable(true);
        this.playerPaint = new Paint();
        playerPaint.setARGB(255, 0, 255, 0);
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
        canvas.drawColor(Color.BLACK);
        canvas.drawCircle(gameState.playerPosX, gameState.playerPosY, gameState.playerSize, playerPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
