package com.simplysplat.hitstrike.gameobject;

import android.content.Context;

import com.simplysplat.hitstrike.GameLoop;

public class GenericEnemy extends Circle {
    private static final double SPEED_PIXELS_PER_SECOND = 300.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private Gameobject target;

    public GenericEnemy(Context context, int color, double x, double y, double radius, Gameobject initialTarget) {
        super(context, color, x, y, radius);
        this.target = initialTarget;
        dirX = 0;
        dirY = 1;
    }

    @Override
    public void update() {
        double distanceToTargetX = target.getX() - x;
        double distanceToTargetY = target.getY() - y;

        double distanceToTarget = Gameobject.getDistanceBetweenObjects(this, target);

        dirX = distanceToTargetX / distanceToTarget;
        dirY = distanceToTargetY / distanceToTarget;

        if (distanceToTarget > 200) {
            velX = dirX * MAX_SPEED;
            velY = dirY * MAX_SPEED;
        } else {
            velX = 0;
            velY = 0;
        }

        x += velX;
        y += velY;
    }
}
