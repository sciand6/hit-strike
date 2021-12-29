package com.simplysplat.hitstrike.gameobject;

import android.content.Context;
import android.graphics.Color;

import com.simplysplat.hitstrike.Game;
import com.simplysplat.hitstrike.GameLoop;

import java.util.List;

public class Bullet extends Circle {
    public static final double SPEED_PIXELS_PER_SECOND = 800.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private String firedBy;
    private Game game;
    private boolean collided = false;

    public Bullet(Context context, Gameobject player, String firedBy, Game game) {
        super(context, firedBy == "Team1" ? Color.GREEN : Color.RED, player.getX(), player.getY(), 8, false, "");
        this.firedBy = firedBy;
        this.game = game;
        velX = player.getDirX() * MAX_SPEED;
        velY = player.getDirY() * MAX_SPEED;
    }

    public boolean getCollided() {
        return collided;
    }

    @Override
    public void update() {
        // Check for collisions
        if (firedBy == "Team2") {
            //Check if bullet is colliding with anyone from team1
            checkForHit(game.getTeam1());
        }
        else {
            //Check if bullet is colliding with anyone from team2
            checkForHit(game.getTeam2());
        }

        x += velX;
        y += velY;
    }

    public void checkForHit(List<Gameobject> team) {
        for (Gameobject entity : team) {
            if (Circle.isColliding((Circle) entity, this) && !entity.isDead) {
                collided = true;
                doDamageOrKill(entity);
            }
        }
    }

    public void doDamageOrKill(Gameobject entity) {
        if (entity.health <= 10) {
            entity.isDead = true;
            if (entity.teamName == "Team1") {
                game.setTeam1DeathCount(game.getTeam1DeathCount() + 1);
            }
            else {
                game.setTeam2DeathCount(game.getTeam2DeathCount() + 1);
            }
        }
        else {
            entity.health = entity.health - 10;
        }
    }
}
