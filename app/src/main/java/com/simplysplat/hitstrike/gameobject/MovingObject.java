package com.simplysplat.hitstrike.gameobject;

import android.content.Context;

// This class contains the attributes a moving object would typically have
public abstract class MovingObject extends Gameobject {
    protected double velX = 0;
    protected double velY = 0;
    protected double dirX = 0;
    protected double dirY = 0;

    public MovingObject(Context context, double x, double y, String teamName) {
        super(context, x, y, teamName);
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public double getDirX() {
        return dirX;
    }

    public void setDirX(double dirX) {
        this.dirX = dirX;
    }

    public double getDirY() {
        return dirY;
    }

    public void setDirY(double dirY) {
        this.dirY = dirY;
    }
}
