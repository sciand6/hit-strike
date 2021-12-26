package com.simplysplat.hitstrike;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.simplysplat.hitstrike.gameobject.Bullet;
import com.simplysplat.hitstrike.gameobject.Circle;
import com.simplysplat.hitstrike.gameobject.Gameobject;
import com.simplysplat.hitstrike.gameobject.Player;
import com.simplysplat.hitstrike.gameobject.Shooter;
import com.simplysplat.hitstrike.gamepanel.Joystick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Game manages all objects in the game and is responsible for updating all states and rendering all
 * objects to the screen
 */
public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private Joystick joystick2;
    private GameLoop gameLoop;
    private Player player;
    private Joystick joystick1;
    private int joystick1PointerId = 0;
    private int joystick2PointerId = 0;
    private List<Bullet> bulletList = new ArrayList<Bullet>();
    private List<Gameobject> team1 = new ArrayList<Gameobject>();
    private List<Gameobject> team2 = new ArrayList<Gameobject>();
    private int numberOfBulletsToSpawn = 0;
    public static Point screenSize;

    public Game(Context context) {
        super(context);
        // Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // Using this to get the screen dimensions
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        this.screenSize = size;

        joystick1 = new Joystick(size.x - 150, size.y - 125, 80, 40);
        joystick2 = new Joystick(150, size.y - 125, 80, 40);

        player = new Player(context, joystick1, joystick2, 200, 200, 15, true);

        team1.add(player);
        team1.add(new Shooter(context, Color.GREEN, 400, 800, 15, false));
        team1.add(new Shooter(context, Color.GREEN, 400, 800, 15, false));
        team1.add(new Shooter(context, Color.GREEN, 400, 800, 15, false));
        team1.add(new Shooter(context, Color.GREEN, 400, 800, 15, false));
        team1.add(new Shooter(context, Color.GREEN, 400, 800, 15, false));
        team1.add(new Shooter(context, Color.GREEN, 400, 800, 15, false));
        team1.add(new Shooter(context, Color.GREEN, 400, 800, 15, false));
        team2.add(new Shooter(context, Color.RED, 1300, 300, 15, false));
        team2.add(new Shooter(context, Color.RED, 1300, 300, 15, false));
        team2.add(new Shooter(context, Color.RED, 1300, 300, 15, false));
        team2.add(new Shooter(context, Color.RED, 1300, 300, 15, false));
        team2.add(new Shooter(context, Color.RED, 1300, 300, 15, false));
        team2.add(new Shooter(context, Color.RED, 1300, 300, 15, false));
        team2.add(new Shooter(context, Color.RED, 1300, 300, 15, false));

        gameLoop = new GameLoop(this, surfaceHolder);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick1.isPressed((double) event.getX(event.getActionIndex()), (double) event.getY(event.getActionIndex()))) {
                    joystick1PointerId = event.getPointerId(event.getActionIndex());
                    joystick1.setIsPressed(true);
                }
                else if (joystick2.isPressed((double) event.getX(event.getActionIndex()), (double) event.getY(event.getActionIndex()))) {
                    joystick2PointerId = event.getPointerId(event.getActionIndex());
                    joystick2.setIsPressed(true);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (joystick1.getIsPressed() && joystick2.getIsPressed()) {
                    joystick1.setActuator((double) event.getX(joystick1PointerId), (double) event.getY(joystick1PointerId));
                    joystick2.setActuator((double) event.getX(joystick2PointerId), (double) event.getY(joystick2PointerId));
                } else if (joystick1.getIsPressed()) {
                    joystick1.setActuator((double) event.getX(), (double) event.getY());
                } else if (joystick2.getIsPressed()) {
                    joystick2.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (joystick1PointerId == event.getPointerId(event.getActionIndex())) {
                    joystick1.setIsPressed(false);
                    joystick1.resetActuator();
                }
                if (joystick2PointerId == event.getPointerId(event.getActionIndex())) {
                    joystick2.setIsPressed(false);
                    joystick2.resetActuator();
                }
                return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        joystick1.draw(canvas);
        joystick2.draw(canvas);

        if (!player.getIsDead())
            player.draw(canvas);

        for (Bullet bullet : bulletList) {
            bullet.draw(canvas);
        }

        for (Gameobject friendly : team1) {
            if (!friendly.getIsDead())
                friendly.draw(canvas);
        }

        for (Gameobject enemy : team2) {
            if (!enemy.getIsDead())
                enemy.draw(canvas);
        }
    }

    public void update() {
        joystick1.update();
        joystick2.update();

        if (!player.getIsDead())
            player.update();

        if (joystick2.getIsPressed() && !player.getIsDead())
            numberOfBulletsToSpawn++;

        if (numberOfBulletsToSpawn != 0 && numberOfBulletsToSpawn == 10) {
            bulletList.add(new Bullet(getContext(), player, "Team1", false));
            numberOfBulletsToSpawn = 0;
        }

        for (Bullet bullet : bulletList) {
            bullet.update();
        }

        for (Gameobject friendly : team1) {
            controlAI(friendly, "Team1", "Team2", team2, bulletList);

            if (!friendly.getIsDead())
                friendly.update();
        }

        for (Gameobject enemy : team2) {
            controlAI(enemy, "Team2", "Team1", team1, bulletList);

            if (!enemy.getIsDead())
                enemy.update();
        }
    }

    public void controlAI(Gameobject entity, String friendlyTeamName, String enemyTeamName, List<Gameobject> enemyList, List<Bullet> bulletList) {
        // Check if this entity was hit
        Iterator<Bullet> bulletIterator = bulletList.iterator();

        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            if (Circle.isColliding((Circle) entity, bullet) && bullet.getIsFiredBy() == enemyTeamName && !entity.getIsDead()) {
                // Remove the bullet before anything
                bulletIterator.remove();
                // Tell whoever is targeting this entity on the opposing team that it's dead
                for (Gameobject enemy : enemyList) {
                    if (enemy.getTarget() == entity) {
                        enemy.setHasTarget(false);
                    }
                }
                // Finally tell the entire game that this entity is dead
                if (entity.getHealth() <= 10) {
                    entity.setIsDead(true);
                } else {
                    // Hardcoding the 10 for now. Will probably store in a variable called damage later.
                    entity.setHealth(entity.getHealth() - 10);
                }
            }
        }

        // If the entity has a live target, then update their direction towards the target and either fire a bullet or increase the updates until their next bullet
        if (entity.getHasTarget() && !entity.getTarget().getIsDead()) {
            // Update the entity to point to their target
            updateEntityDirection(entity);
            // Should probably make this hardcoded 50 a variable, but it's ok for now
            // Either way if they meet the updates until fire criteria, and they aren't dead or the player
            // then fire a bullet
            if (entity.getFireRate() == 50 && !entity.getIsDead() && !entity.getIsPlayer()) {
                bulletList.add(new Bullet(getContext(), entity, friendlyTeamName, false));
                entity.setFireRate(0);// Reset updates until next bullet
            }
            else {
                entity.setFireRate(entity.getFireRate() + 1);
            }
        }
        else {
            // Just pick any target that isn't dead (will probably change this logic sooner or later)
            for (Gameobject enemy : enemyList) {
                if (!enemy.getIsDead()) {
                    entity.setHasTarget(true);
                    entity.setTarget(enemy);
                }
            }
        }
    }

    public void updateEntityDirection(Gameobject entity) {
        double distanceToTargetX = entity.getTarget().getX() - entity.getX();
        double distanceToTargetY = entity.getTarget().getY() - entity.getY();

        double distanceToTarget = Gameobject.getDistanceBetweenObjects(entity, entity.getTarget());

        entity.setDirX(distanceToTargetX / distanceToTarget);
        entity.setDirY(distanceToTargetY / distanceToTarget);
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}
