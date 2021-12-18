package com.simplysplat.hitstrike.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.simplysplat.hitstrike.GameLoop;
import com.simplysplat.hitstrike.gamepanel.Joystick;

public class Player extends Circle {
    public static final double SPEED_PIXELS_PER_SECOND = 400.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private Joystick joystick;
    private Context context;

    public Player(Context context, Joystick joystick, double x, double y, double radius) {
        super(context, Color.YELLOW, x, y, radius);
        this.context = context;
        this.joystick = joystick;
    }

    public void update() {
        velX = joystick.getActuatorX() * MAX_SPEED;
        velY = joystick.getActuatorY() * MAX_SPEED;

        x += velX;
        y += velY;
    }
}
