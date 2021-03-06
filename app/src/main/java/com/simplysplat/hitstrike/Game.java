package com.simplysplat.hitstrike;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.simplysplat.hitstrike.gameobject.MovingObject;
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
    private List<Bullet> bulletList = new ArrayList<>();
    private List<Gameobject> team1 = new ArrayList<>();
    private List<Gameobject> team2 = new ArrayList<>();
    private int numberOfBulletsToSpawn = 1;
    public static Point screenSize;
    private int level = 1;
    private int friendlyCount = 2;
    private int team1DeathCount = 0;
    private int team2DeathCount = 0;

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

        startNewLevel();

        gameLoop = new GameLoop(this, surfaceHolder);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick1.isPressed(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()))) {
                    joystick1PointerId = event.getPointerId(event.getActionIndex());
                    joystick1.setIsPressed(true);
                }
                else if (joystick2.isPressed(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()))) {
                    joystick2PointerId = event.getPointerId(event.getActionIndex());
                    joystick2.setIsPressed(true);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (joystick1.getIsPressed() && joystick2.getIsPressed()) {
                    joystick1.setActuator(event.getX(joystick1PointerId), event.getY(joystick1PointerId));
                    joystick2.setActuator(event.getX(joystick2PointerId), event.getY(joystick2PointerId));
                } else if (joystick1.getIsPressed()) {
                    joystick1.setActuator(event.getX(), event.getY());
                } else if (joystick2.getIsPressed()) {
                    joystick2.setActuator(event.getX(), event.getY());
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

        // Draw HUD which is just text for now
        Paint levelTextPaint = new Paint();
        levelTextPaint.setColor(Color.WHITE);
        levelTextPaint.setTextSize(60);

        canvas.drawText("Level: " + level, 100, 100, levelTextPaint);

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
        if (determineLoser() == 1) {
            startNewLevel();
        } else if (determineLoser() == 2) {
            level++;
            startNewLevel();
        }

        joystick1.update();
        joystick2.update();

        if (!player.getIsDead())
            player.update();

        if (joystick2.getIsPressed() && !player.getIsDead())
            numberOfBulletsToSpawn++;

        if (numberOfBulletsToSpawn == 10) {
            bulletList.add(new Bullet(getContext(), player, "Team1", this));
            numberOfBulletsToSpawn = 0;
        }

        Iterator<Bullet> bulletIterator = bulletList.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();

            if (bullet.getX() > screenSize.x || bullet.getY() > screenSize.y
                    || bullet.getX() < 0 || bullet.getY() < 0
                    || bullet.getCollided()) {
                bulletIterator.remove();
            }
            else {
                bullet.update();
            }
        }

        for (Gameobject friendly : team1) {
            if (!friendly.getIsDead())
                friendly.update();
        }

        for (Gameobject enemy : team2) {
            if (!enemy.getIsDead())
                enemy.update();
        }
    }

    public void addBullet(MovingObject entity, String teamName) {
        bulletList.add(new Bullet(getContext(), entity, teamName, this));
    }

    public List<Gameobject> getTeam1() {
        return team1;
    }

    public List<Gameobject> getTeam2() {
        return team2;
    }

    public int getTeam1DeathCount() {
        return team1DeathCount;
    }

    public void setTeam1DeathCount(int team1DeathCount) {
        this.team1DeathCount = team1DeathCount;
    }

    public int getTeam2DeathCount() {
        return team2DeathCount;
    }

    public void setTeam2DeathCount(int team2DeathCount) {
        this.team2DeathCount = team2DeathCount;
    }

    public int determineLoser() {
        // If team 1 loses return 1
        if (team1DeathCount == team1.size()) {
            return 1;
        }
        // If team 2 loses return 2
        else if (team2DeathCount == team2.size()) {
            return 2;
        }

        // Default return -1
        return -1;
    }

    public void startNewLevel() {
        if (level % 2 == 0)
            friendlyCount++;

        team1.clear();
        team2.clear();
        team1DeathCount = 0;
        team2DeathCount = 0;

        player = new Player(getContext(), joystick1, joystick2, 300, screenSize.y / 2);

        team1.add(player);

        // Spawn friendlies
        for (int i = 0; i < friendlyCount; i++) {
            team1.add(new Shooter(getContext(), Color.GREEN, 300, screenSize.y / 2, "Team1", this));
        }

        // Spawn enemies
        for (int i = 0; i < level; i++) {
            team2.add(new Shooter(getContext(), Color.RED, screenSize.x - 300, screenSize.y / 2, "Team2", this));
        }
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}
