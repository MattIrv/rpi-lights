package com.example.zanemayberry.webandmobile;

import android.graphics.Canvas;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;import java.lang.Override;import java.lang.System;import java.lang.Thread;

/**
 * Created by zanemayberry on 11/9/14.
 */
public class GameThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private GameState gameState;
    float pitch = 0;
    float roll = 0;
    int width = 0;
    int height = 0;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView, int width, int height) {
        super();
        System.out.println("Thread initialized.");
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        this.gameState = new GameState();
        this.width = width;
        this.height = height;
        this.gameState.playerPosX = (float) (width/2 - 25);
        this.gameState.playerPosY = (float) (height/2 - 25);
        this.gameState.playerSize = 50.0f;
    }

    private boolean running;
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long tickCount = 0L;
        System.out.println("Starting game loop...");
        while (running) {
            System.out.println("TickCount: " + tickCount);
            Canvas canvas = this.surfaceHolder.lockCanvas();
            tickCount++;
            gameView.render(canvas, gameState);
            surfaceHolder.unlockCanvasAndPost(canvas);
            if (checkX())
                gameState.playerPosX += 10*roll;
            if (checkY())
                gameState.playerPosY -= 10*pitch;
        }
        System.out.println("Loop Executed " + tickCount + " times.");
    }

    private boolean checkX() {
        if (gameState.playerPosX + 10*roll + 50 > width) {
            if (gameState.playerPosX + 50 > width)
                gameState.playerPosX = width - 51;
            return false;
        }
        if (gameState.playerPosX + 10*roll < 50) {
            if (gameState.playerPosX < 50)
                gameState.playerPosX = 51;
            return false;
        }
        return true;
    }

    private boolean checkY() {
        if (gameState.playerPosY - 10*pitch + 50 > height) {
            if (gameState.playerPosY + 50 > height)
                gameState.playerPosY = height - 51;
            return false;
        }
        if (gameState.playerPosY - 10*pitch < 50) {
            if (gameState.playerPosY < 50)
                gameState.playerPosY = 51;
            return false;
        }
        return true;
    }

    public void updateOrientation(float[] orientationVec) {
        // Azimuth, Pitch, Roll
        pitch = orientationVec[1];
        roll = orientationVec[2];
    }
}
