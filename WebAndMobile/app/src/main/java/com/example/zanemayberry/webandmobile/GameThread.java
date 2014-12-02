package com.example.zanemayberry.webandmobile;

import android.content.Intent;
import android.graphics.Canvas;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;import java.lang.Override;import java.lang.System;import java.lang.Thread;
import java.util.ArrayList;
import java.util.Random;

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
    int rotation = 0;
    int playerSpeed = 10;
    int gravitySpeed = 5;
    long lastBlink = 0;
    long lastSpawn = 0;


    public GameThread(SurfaceHolder surfaceHolder, GameView gameView, int width, int height, int rotation) {
        super();
        System.out.println("Thread initialized.");
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        this.gameState = new GameState();
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.gameState.playerPosX = (float) (width/2 - 25);
        this.gameState.playerPosY = (float) (height/2 - 25);
        this.gameState.playerSize = 25.0f;
        this.gameState.score = 0;
        this.gameState.curDir = Direction.random();
        this.gameState.nextDir = this.gameState.curDir.diffRandom();
        this.gameState.timeOfChange = System.currentTimeMillis() + 6000;
        this.gameState.isBlinked = false;
        this.gameState.enemyStateList = new ArrayList<EnemyState>();
        this.gameState.isChangingSoon = false;
    }

    private boolean running;
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long tickCount = 0L;
        System.out.println("Starting game loop...");
        boolean hasStarted = false;
        while (running) {
            //System.out.println("TickCount: " + tickCount);
            Canvas canvas = this.surfaceHolder.lockCanvas();
            tickCount++;
            gameState.score++;
            gameView.render(canvas, gameState, tickCount);
            surfaceHolder.unlockCanvasAndPost(canvas);
            if (this.gameState.timeOfChange - System.currentTimeMillis() < 3000) {
                gameState.isChangingSoon = true;
            }
            if (gameState.isChangingSoon) {
                if (System.currentTimeMillis() - lastBlink > 500) {
                    this.gameState.isBlinked = !this.gameState.isBlinked;
                    lastBlink = System.currentTimeMillis();
                    this.gameState.updateLights = true;
                }
            }
            if (this.gameState.timeOfChange < System.currentTimeMillis()) {
                this.gameState.curDir = this.gameState.nextDir;
                this.gameState.nextDir = this.gameState.curDir.diffRandom();
                this.gameState.timeOfChange += 6000;
                this.gameState.isBlinked = false;
                this.gameState.isChangingSoon = false;
                this.gameState.updateLights = true;
                hasStarted = true;
            }
            if (!hasStarted) {
                continue;
            }
            if (checkX()) {
                gameState.playerPosX += playerSpeed * roll
                        + gravitySpeed * gameState.curDir.hComponent();
            } else {
                gameView.gameOver(gameState);
                return;
            }
            if (checkY()) {
                gameState.playerPosY -= playerSpeed * pitch
                        + gravitySpeed * gameState.curDir.vComponent();
            } else {
                gameView.gameOver(gameState);
                return;
            }
            for (int i = gameState.enemyStateList.size() - 1; i >= 0; i--) {
                EnemyState eState = gameState.enemyStateList.get(i);
                eState.posX += gravitySpeed * gameState.curDir.hComponent();
                eState.posY -= gravitySpeed * gameState.curDir.vComponent();
                if (eState.posX > width || eState.posX < 0 || eState.posY > height || eState.posY < 0) {
                    gameState.enemyStateList.remove(i);
                } else if (doesCollide(eState)) {
                    gameView.gameOver(gameState);
                    return;
                }
            }
            if (System.currentTimeMillis() > lastSpawn + 400) {
                EnemyState newEnemy = new EnemyState();
                newEnemy.size = 25;
                Random randy = new Random();
                if (this.gameState.curDir == Direction.UP) {
                    newEnemy.posX = randy.nextInt(width - 2 * gameState.edgeSize - 2 * newEnemy.size) + gameState.edgeSize + newEnemy.size;
                    newEnemy.posY = height;
                } else if (this.gameState.curDir == Direction.RIGHT) {
                    newEnemy.posX = 0;
                    newEnemy.posY = randy.nextInt(height - 2 * gameState.edgeSize - 2 * newEnemy.size) + gameState.edgeSize + newEnemy.size;
                } else if (this.gameState.curDir == Direction.DOWN) {
                    newEnemy.posX = randy.nextInt(width - 2 * gameState.edgeSize - 2 * newEnemy.size) + gameState.edgeSize + newEnemy.size;
                    newEnemy.posY = 0;
                } else if (this.gameState.curDir == Direction.LEFT) {
                    newEnemy.posX = width;
                    newEnemy.posY = randy.nextInt(height - 2 * gameState.edgeSize - 2 * newEnemy.size) + gameState.edgeSize + newEnemy.size;
                }
                this.gameState.enemyStateList.add(newEnemy);
                lastSpawn = System.currentTimeMillis();
            }
        }
        System.out.println("Loop Executed " + tickCount + " times.");
    }

    private boolean checkX() {
        if (gameState.playerPosX + playerSpeed*roll + gameState.playerSize > width - gameState.edgeSize) {
            if (gameState.playerPosX + gameState.playerSize > width)
                gameState.playerPosX = width - (gameState.playerSize+1);
            return false;
        }
        if (gameState.playerPosX + playerSpeed*roll < gameState.playerSize + gameState.edgeSize) {
            if (gameState.playerPosX < gameState.playerSize)
                gameState.playerPosX = (gameState.playerSize+1);
            return false;
        }
        return true;
    }

    private boolean checkY() {
        if (gameState.playerPosY - playerSpeed*pitch + gameState.playerSize > height - gameState.edgeSize) {
            if (gameState.playerPosY + gameState.playerSize > height)
                gameState.playerPosY = height - (gameState.playerSize+1);
            return false;
        }
        if (gameState.playerPosY - playerSpeed*pitch < gameState.playerSize + gameState.edgeSize) {
            if (gameState.playerPosY < gameState.playerSize)
                gameState.playerPosY = (gameState.playerSize+1);
            return false;
        }
        return true;
    }

    private boolean doesCollide(EnemyState eState) {
        float deltaX = gameState.playerPosX - eState.posX;
        float deltaY = gameState.playerPosY - eState.posY;
        if (Math.hypot(deltaX, deltaY) < eState.size + gameState.playerSize) {
            return true;
        }
        return false;
    }

    public void updateOrientation(float[] orientationVec) {
        // Azimuth, Pitch, Roll
        switch (rotation) {
            case Surface.ROTATION_0:
                pitch = orientationVec[1];
                roll = orientationVec[2];
                break;
            case Surface.ROTATION_90:
                pitch = -1*orientationVec[2];
                roll = orientationVec[1];
                break;
            case Surface.ROTATION_180:
                pitch = -1*orientationVec[1];
                roll = -1*orientationVec[2];
                break;
            case Surface.ROTATION_270:
                pitch = orientationVec[2];
                roll = -1*orientationVec[1];
                break;
        }
    }
}
