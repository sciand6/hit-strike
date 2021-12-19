package com.simplysplat.hitstrike.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.simplysplat.hitstrike.GameLoop;
import com.simplysplat.hitstrike.MathUtils;
import com.simplysplat.hitstrike.gamepanel.Joystick;

public class Player extends Circle {
    public static final double SPEED_PIXELS_PER_SECOND = 400.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private Joystick joystick2;
    private Joystick joystick1;
    private Context context;
    private double dirX;
    private double dirY;

    public Player(Context context, Joystick joystick1, Joystick joystick2, double x, double y, double radius) {
        super(context, Color.YELLOW, x, y, radius);
        this.context = context;
        this.joystick1 = joystick1;
        this.joystick2 = joystick2;
    }

    public void update() {
        velX = joystick1.getActuatorX() * MAX_SPEED;
        velY = joystick1.getActuatorY() * MAX_SPEED;

        x += velX;
        y += velY;

        double velX2 = joystick2.getActuatorX();
        double velY2 = joystick2.getActuatorY();

        if (velX2 != 0 || velY2 != 0) {
            double distance = MathUtils.getDistance(0, 0, velX2, velY2);
            dirX = velX2 / distance;
            dirY = velY2 / distance;
        }
    }

    public double getDirY() {
        return dirY;
    }

    public double getDirX() {
        return dirX;
    }
}
