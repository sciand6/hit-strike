package com.simplysplat.hitstrike.gameobject;

import android.content.Context;
import android.graphics.Color;

import com.simplysplat.hitstrike.GameLoop;

public class Bullet extends Circle {
    public static final double SPEED_PIXELS_PER_SECOND = 400.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private EntityTag firedBy;

    public Bullet(Context context, Circle shooter, EntityTag firedBy) {
        super(context, Color.WHITE, shooter.getX(), shooter.getY(), 10, EntityTag.BULLET);
        velX = shooter.getDirX() * MAX_SPEED;
        velY = shooter.getDirY() * MAX_SPEED;
        this.firedBy = firedBy;
    }

    @Override
    public void update() {
        x += velX;
        y += velY;
    }

    public EntityTag firedBy() {
        return firedBy;
    }

}
