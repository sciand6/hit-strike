package com.simplysplat.hitstrike;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {

    private SurfaceHolder surfaceHolder;
    private Game game;
    private Canvas canvas;
    private boolean isRunning;
    public static final double MAX_UPS = 30.0;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;

    public GameLoop(Game game, SurfaceHolder surfaceHolder) {
        this.game = game;
        this.surfaceHolder = surfaceHolder;
    }

    /**
     * Used to invoke the thread (start() is essentially calling run())
     */
    public void startLoop() {
        isRunning = true;
        start();
    }

    /**
     * Comes from the Thread class: This gets run when we start() the thread.
     * Holds the internal logic of the game loop.
     */
    @Override
    public void run() {
        super.run();

        // Time and cycle count
        int updateCount = 0;

        // Game loop
        long startTime = System.currentTimeMillis();
        while (isRunning) {
            // Update and render the game
            try {
                // Get the canvas and send to the Game object for painting operations
                canvas = surfaceHolder.lockCanvas();

                synchronized (surfaceHolder) {
                    game.update();
                    updateCount++;
                    game.draw(canvas);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                try {
                    // Draw to canvas and update frame count
                    surfaceHolder.unlockCanvasAndPost(canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            long sleepTime = (long) (updateCount*UPS_PERIOD - elapsedTime);
            if(sleepTime > 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Skip frames to keep up with target UPS
            while(sleepTime < 0 && updateCount < MAX_UPS-1) {
                game.update();
                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long) (updateCount*UPS_PERIOD - elapsedTime);
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            // For every second
            if (elapsedTime >= 1000) {
                // Reset the frame count and start time
                updateCount = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }

    public void stopLoop() {
        isRunning = false;
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
