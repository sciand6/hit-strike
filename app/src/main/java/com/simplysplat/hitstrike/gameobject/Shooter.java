package com.simplysplat.hitstrike.gameobject;

import android.content.Context;
import android.util.Log;

import com.simplysplat.hitstrike.GameLoop;

public class Shooter extends Circle {
    private static final double SPEED_PIXELS_PER_SECOND = 100.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private int bulletsToSpawn = 0;
    private Gameobject target;
    private int botId;
    private double currentTargetDistance;

    public Shooter(Context context, int color, double x, double y, double radius, Gameobject initialTarget, EntityTag entity, int botId) {
        super(context, color, x, y, radius, entity);
        this.target = initialTarget;
        this.botId = botId;
        dirX = 0;
        dirY = 1;
    }

    @Override
    public void update() {
        double distanceToTargetX = target.getX() - x;
        double distanceToTargetY = target.getY() - y;
        bulletsToSpawn++;

        currentTargetDistance = Gameobject.getDistanceBetweenObjects(this, target);

        dirX = distanceToTargetX / currentTargetDistance;
        dirY = distanceToTargetY / currentTargetDistance;

        if (currentTargetDistance > 300) {
            velX = dirX * MAX_SPEED;
            velY = dirY * MAX_SPEED;
        } else {
            velX = 0;
            velY = 0;
        }

        x += velX;
        y += velY;
    }

    public double getCurrentTargetDistance() {
        return currentTargetDistance;
    }

    public void setTarget(Gameobject target) {
        this.target = target;
    }

    public int getBotId() {
        return botId;
    }

    public int getBulletsToSpawn() {
        return bulletsToSpawn;
    }

    public void setBulletsToSpawn(int bulletsToSpawn) {
        this.bulletsToSpawn = bulletsToSpawn;
    }
}
