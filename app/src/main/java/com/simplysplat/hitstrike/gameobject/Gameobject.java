package com.simplysplat.hitstrike.gameobject;

import android.graphics.Canvas;

public abstract class Gameobject {
    protected double x;
    protected double y;
    protected double velX;
    protected double velY;
    protected boolean isDead = false;
    protected double dirX;
    protected double dirY;
    protected boolean hasTarget = false;
    protected Gameobject target;
    protected int fireRate = 0;
    protected boolean isPlayer;

    public Gameobject(double x, double y, boolean isPlayer) {
        this.isPlayer = isPlayer;
        this.x = x;
        this.y = y;
    }

    public abstract void draw(Canvas canvas);

    public abstract void update();

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean getIsDead() {
        return isDead;
    }

    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    public void setDirX(double dirX) {
        this.dirX = dirX;
    }

    public double getDirX() {
        return dirX;
    }

    public void setDirY(double dirY) {
        this.dirY = dirY;
    }

    public double getDirY() {
        return dirY;
    }

    public boolean getHasTarget() {
        return hasTarget;
    }

    public void setHasTarget(boolean hasTarget) {
        this.hasTarget = hasTarget;
    }

    public Gameobject getTarget() {
        return target;
    }

    public void setTarget(Gameobject target) {
        this.target = target;
    }

    public int getFireRate() {
        return fireRate;
    }

    public void setFireRate(int i) {
        this.fireRate = i;
    }

    public double clamp(double pos, double min, double max) {
        if (pos < min) {
            return min;
        } else if (pos > max) {
            return max;
        }

        return pos;
    }

    public static double getDistanceBetweenObjects(Gameobject o1, Gameobject o2) {
        return Math.sqrt(
                Math.pow(o2.getX() - o1.getX(), 2) +
                        Math.pow(o2.getY() - o1.getY(), 2)
        );
    }

    public boolean getIsPlayer() {
        return isPlayer;
    }
}
