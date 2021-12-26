package com.simplysplat.hitstrike.gameobject;

import android.content.Context;
import android.graphics.Color;

import com.simplysplat.hitstrike.GameLoop;

public class Bullet extends Circle {
    public static final double SPEED_PIXELS_PER_SECOND = 800.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private String firedBy;

    public Bullet(Context context, Gameobject player, String firedBy, boolean isPlayer) {
        super(context, firedBy == "Team1" ? Color.GREEN : Color.RED, player.getX(), player.getY(), 10, isPlayer);
        this.firedBy = firedBy;
        velX = player.getDirX() * MAX_SPEED;
        velY = player.getDirY() * MAX_SPEED;
    }

    public String getIsFiredBy() {
        return firedBy;
    }

    @Override
    public void update() {
        x += velX;
        y += velY;
    }
}
