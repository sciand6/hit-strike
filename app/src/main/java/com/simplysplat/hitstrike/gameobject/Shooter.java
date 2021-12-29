package com.simplysplat.hitstrike.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.simplysplat.hitstrike.Game;

import java.util.ArrayList;

public class Shooter extends Circle {
    private int updatesUntilDirectionChange = 74;
    private Game game;

    public Shooter(Context context, int color, double x, double y, String teamName, Game game) {
        super(context, color, x, y, 25, false, teamName);
        this.game = game;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // draw health
        drawHealthBar(canvas);
    }

    @Override
    public void update() {
        updatesUntilDirectionChange++;
        if (hasTarget) {
            if (target.isDead)
                hasTarget = false;
            else {
                // Update direction
                double distanceToTargetX = target.x - x;
                double distanceToTargetY = target.y - y;

                double distanceToTarget = getDistanceBetweenObjects(this, target);

                dirX = distanceToTargetX / distanceToTarget;
                dirY = distanceToTargetY / distanceToTarget;

                // Shoot bullet
                if (fireRate == 50) {
                    game.addBullet(this, teamName);
                    fireRate = 0;
                } else {
                    fireRate++;
                }
            }
        }
        else {
            // Look for new target
            if (teamName == "Team1") {
                // Look in team2 list
                for (Gameobject enemy : game.getTeam2()) {
                    if (!enemy.isDead) {
                        hasTarget = true;
                        target = enemy;
                    }
                }
            }
            else {
                for (Gameobject enemy : game.getTeam1()) {
                    if (!enemy.isDead) {
                        hasTarget = true;
                        target = enemy;
                    }
                }
            }
        }

        if (updatesUntilDirectionChange >= 75) {
            velX = ((Math.random() * (1 - -1)) + -1) * 10.0;
            velY = ((Math.random() * (1 - -1)) + -1) * 10.0;
            updatesUntilDirectionChange = 0;
        }

        x = clamp(x, 100, Game.screenSize.x - 100);
        y = clamp(y, 100, Game.screenSize.y - 100);

        x += velX;
        y += velY;
    }
}
