package com.simplysplat.hitstrike.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.simplysplat.hitstrike.Game;

import java.util.ArrayList;

public class Shooter extends Circle {
    private int updates = 74;

    public Shooter(Context context, int color, double x, double y, double radius, boolean isPlayer) {
        super(context, color, x, y, radius, isPlayer);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // draw health
        drawHealthBar(canvas);
    }

    @Override
    public void update() {
        updates++;

        if (updates >= 75) {
            velX = ((Math.random() * (1 - -1)) + -1) * 10.0;
            velY = ((Math.random() * (1 - -1)) + -1) * 10.0;
            updates = 0;
        }

        x = clamp(x, 100, Game.screenSize.x - 100);
        y = clamp(y, 100, Game.screenSize.y - 100);

        x += velX;
        y += velY;
    }
}
