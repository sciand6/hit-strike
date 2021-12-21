package com.simplysplat.hitstrike;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.simplysplat.hitstrike.gameobject.Bullet;
import com.simplysplat.hitstrike.gameobject.Circle;
import com.simplysplat.hitstrike.gameobject.GenericEnemy;
import com.simplysplat.hitstrike.gameobject.Player;
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
    private List<GenericEnemy> enemyList = new ArrayList<GenericEnemy>();
    private int numberOfBulletsToSpawn = 0;

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

        joystick1 = new Joystick(size.x - 150, size.y - 125, 80, 40);
        joystick2 = new Joystick(150, size.y - 125, 80, 40);

        player = new Player(context, joystick1, joystick2, 200, 200, 25);

        enemyList.add(new GenericEnemy(context, Color.RED, 300, 200, 30, player));
        enemyList.add(new GenericEnemy(context, Color.RED, 400, 200, 30, player));
        enemyList.add(new GenericEnemy(context, Color.RED, 500, 200, 30, player));

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
        player.draw(canvas);

        for (GenericEnemy enemy : enemyList) {
            enemy.draw(canvas);
        }

        for (Bullet bullet : bulletList) {
            bullet.draw(canvas);
        }
    }

    public void update() {
        joystick1.update();
        joystick2.update();
        player.update();

        if (joystick2.getIsPressed())
            numberOfBulletsToSpawn++;

        if (numberOfBulletsToSpawn != 0 && numberOfBulletsToSpawn % 10 == 0) {
            for (GenericEnemy enemy : enemyList) {
                bulletList.add(new Bullet(getContext(), enemy, Bullet.ShooterTag.ENEMY));
            }
            bulletList.add(new Bullet(getContext(), player, Bullet.ShooterTag.PLAYER));
        }

        for (Bullet bullet : bulletList) {
            bullet.update();
        }

        for (GenericEnemy enemy : enemyList) {
            enemy.update();
        }

        Iterator<Bullet> bulletIterator = bulletList.iterator();

        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();

            if (Circle.isColliding(player, bullet) && bullet.firedBy() == Bullet.ShooterTag.ENEMY) {
                // TODO: Health logic or game over
                bulletIterator.remove();
                break;
            }

            Iterator<GenericEnemy> enemyIterator = enemyList.iterator();
            while (enemyIterator.hasNext()) {
                Circle enemy = enemyIterator.next();
                if (Circle.isColliding(enemy, bullet) && bullet.firedBy() == Bullet.ShooterTag.PLAYER) {
                    enemyIterator.remove();
                    bulletIterator.remove();
                    break;
                }
            }
        }
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}
