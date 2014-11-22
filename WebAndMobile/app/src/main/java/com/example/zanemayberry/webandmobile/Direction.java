package com.example.zanemayberry.webandmobile;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by zanemayberry on 11/22/14.
 */
public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public int vComponent() {
        if (this == UP) {
            return 1;
        } else if (this == DOWN) {
            return -1;
        }
        return 0;
    }

    public int hComponent() {
        if (this == RIGHT) {
            return 1;
        } else if (this == LEFT) {
            return -1;
        }
        return 0;
    }

    public int getColor() {
        if (this == UP) {
            return Color.BLUE;
        } else if (this == RIGHT) {
            return Color.RED;
        } else if (this == DOWN) {
            return Color.GREEN;
        } else if (this == LEFT) {
            return Color.WHITE;
        }
        return -1;
    }

    public static Direction random() {
        Random randy = new Random();
        int rand = randy.nextInt(4);
        if (rand == 0) {
            return UP;
        } else if (rand == 1) {
            return RIGHT;
        } else if (rand == 2) {
            return DOWN;
        } else if (rand == 3) {
            return LEFT;
        }
        return null;
    }

    public Direction diffRandom() {
        Random randy = new Random();
        int rand = randy.nextInt(3) + 1;
        int i = 0;
        if (this == Direction.UP) {
            i = 0;
        } else if (this == Direction.RIGHT) {
            i = 1;
        } else if (this == Direction.DOWN) {
            i = 2;
        } else if (this == Direction.LEFT) {
            i = 3;
        }
        int j = i + rand;
        j %= 4;
        if (j == 0) {
            return UP;
        } else if (j == 1) {
            return RIGHT;
        } else if (j == 2) {
            return DOWN;
        } else if (j == 3) {
            return LEFT;
        }
        return null;
    }
}
