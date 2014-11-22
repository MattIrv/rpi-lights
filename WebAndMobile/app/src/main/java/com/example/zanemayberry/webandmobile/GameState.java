package com.example.zanemayberry.webandmobile;

import android.graphics.Path;

import java.util.ArrayList;

/**
 * Created by zanemayberry on 11/9/14.
 */
public class GameState {
    public float playerPosX;
    public float playerPosY;
    public float playerSize;
    public int score;
    public Direction curDir;
    public Direction nextDir;
    public long timeOfChange;
    public boolean isBlinked;
    public ArrayList<EnemyState> enemyStateList;
    public int edgeSize = 50;
    public boolean updateLights = true;
    public boolean isChangingSoon = false;
}
