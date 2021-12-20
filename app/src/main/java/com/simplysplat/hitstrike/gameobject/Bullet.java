package com.simplysplat.hitstrike.gameobject;

import android.content.Context;
import android.graphics.Color;

import com.simplysplat.hitstrike.GameLoop;

public class Bullet extends Circle {
    public static final double SPEED_PIXELS_PER_SECOND = 800.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;

    public Bullet(Context context, Player player) {
        super(context, Color.WHITE, player.getX(), player.getY(), 10);
        velX = player.getDirX() * MAX_SPEED;
        velY = player.getDirY() * MAX_SPEED;
    }

    @Override
    public void update() {
        x += velX;
        y += velY;
    }
}
