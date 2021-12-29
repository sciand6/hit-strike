package com.simplysplat.hitstrike.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.simplysplat.hitstrike.R;

public abstract class Gameobject {
    protected final float maxHealth = 100;
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
    protected float health = maxHealth;
    protected Context context;
    protected String teamName;

    public Gameobject(Context context, double x, double y, boolean isPlayer, String teamName) {
        this.context = context;
        this.isPlayer = isPlayer;
        this.x = x;
        this.y = y;
        this.teamName = teamName;
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

    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
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

    // Only some gameobjects will have a healthbar
    public void drawHealthBar(Canvas canvas) {
        Paint borderPaint = new Paint();
        int borderColor = ContextCompat.getColor(context, R.color.healthBarBorder);
        borderPaint.setColor(borderColor);

        Paint healthPaint = new Paint();
        int healthColor = ContextCompat.getColor(context, R.color.healthBarHealth);
        healthPaint.setColor(healthColor);

        int height = 20;
        int width = 100;
        int margin = 2;
        float distanceToObject = 25;
        float healthPercentage = health / maxHealth;

        // draw border
        float borderLeft, borderTop, borderRight, borderBottom;
        borderLeft = (float) (x - width / 2);
        borderRight = (float) (x + width / 2);
        borderBottom = (float) (y - distanceToObject);
        borderTop = borderBottom - height;
        canvas.drawRect(
                borderLeft,
                borderTop,
                borderRight,
                borderBottom,
                borderPaint
        );

        // draw health
        float healthLeft, healthTop, healthRight, healthBottom, healthWidth, healthHeight;
        healthWidth = width - 2 * margin;
        healthHeight = height - 2 * margin;
        healthLeft = borderLeft + margin;
        healthRight = borderLeft + healthWidth * healthPercentage;
        healthBottom = borderBottom - margin;
        healthTop = healthBottom - healthHeight;
        canvas.drawRect(
                healthLeft,
                healthTop,
                healthRight,
                healthBottom,
                healthPaint
        );
    }
}
