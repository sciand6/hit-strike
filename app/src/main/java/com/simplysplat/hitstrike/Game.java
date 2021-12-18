package com.simplysplat.hitstrike;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.simplysplat.hitstrike.gameobject.Bullet;
import com.simplysplat.hitstrike.gameobject.Player;
import com.simplysplat.hitstrike.gamepanel.Joystick;

import java.util.ArrayList;
import java.util.List;

/**
 * Game manages all objects in the game and is responsible for updating all states and rendering all
 * objects to the screen
 */
public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private GameLoop gameLoop;
    private Player player;
    private Joystick joystick;
    private int joystickPointerId = 0;
    private List<Bullet> bulletList = new ArrayList<Bullet>();
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

        joystick = new Joystick(size.x - 300, size.y - 200, 80, 40);
        player = new Player(context, joystick, 200, 200, 50);

        gameLoop = new GameLoop(this, surfaceHolder);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick.getIsPressed()) {
                    numberOfBulletsToSpawn++;
                }
                else if (joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    joystickPointerId = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                }
                else {
                    numberOfBulletsToSpawn++;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (joystick.getIsPressed()) {
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (joystickPointerId == event.getPointerId(event.getActionIndex())) {
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
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

        joystick.draw(canvas);
        player.draw(canvas);

        for (Bullet bullet : bulletList) {
            bullet.draw(canvas);
        }
    }

    public void update() {
        joystick.update();
        player.update();

        while (numberOfBulletsToSpawn > 0) {
            bulletList.add(new Bullet(getContext()));
            numberOfBulletsToSpawn--;
        }
        for (Bullet bullet : bulletList) {
            bullet.update();
        }
    }
}
