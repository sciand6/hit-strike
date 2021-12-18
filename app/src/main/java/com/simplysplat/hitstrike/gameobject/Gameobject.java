package com.simplysplat.hitstrike.gameobject;

import android.graphics.Canvas;

public abstract class Gameobject {
    protected double x;
    protected double y;
    protected double velX;
    protected double velY;

    public Gameobject() {

    }

    public Gameobject(double x, double y) {
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
}
