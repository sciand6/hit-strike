package com.simplysplat.hitstrike.gameobject;

import android.content.Context;

public abstract class AI extends MovingObject {
    protected boolean hasTarget = false;
    protected int fireRate = 0;
    protected Gameobject target;

    public AI(Context context, double x, double y, String teamName) {
        super(context, x, y, teamName);
    }

    public boolean getHasTarget() {
        return hasTarget;
    }

    public void setHasTarget(boolean hasTarget) {
        this.hasTarget = hasTarget;
    }

    public int getFireRate() {
        return fireRate;
    }

    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

    public Gameobject getTarget() {
        return target;
    }

    public void setTarget(Gameobject target) {
        this.target = target;
    }
}
