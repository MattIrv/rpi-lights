package com.example.zanemayberry.webandmobile;

import android.graphics.Canvas;
import android.view.Surface;
import android.view.SurfaceHolder;import java.lang.Override;import java.lang.System;import java.lang.Thread;

/**
 * Created by zanemayberry on 11/9/14.
 */
public class GameThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private GameState gameState;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        System.out.println("Thread initialized.");
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        this.gameState = new GameState();
        this.gameState.playerPosX = 0.0f;
        this.gameState.playerPosY = 0.0f;
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
            gameState.playerPosX += 1f;
            gameState.playerPosY += 1f;
        }
        System.out.println("Loop Executed " + tickCount + " times.");
    }
}
