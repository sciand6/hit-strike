package com.simplysplat.hitstrike.gamepanel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.simplysplat.hitstrike.MathUtils;

public class Joystick {
    private Paint innerCirclePaint;
    private Paint outerCirclePaint;
    private int innerCircleRadius;
    private int outerCircleRadius;
    private int outerCircleX;
    private int outerCircleY;
    private int innerCircleX;
    private int innerCircleY;
    private double joystickCenterToTouchDistance;
    private double actuatorX = 0;
    private double actuatorY = 0;
    private boolean isPressed;

    public Joystick(int centerX, int centerY, int outerCircleRadius, int innerCircleRadius) {
        this.outerCircleX = centerX;
        this.outerCircleY = centerY;
        this.innerCircleX = centerX;
        this.innerCircleY = centerY;

        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;

        // paint of circles
        this.outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        this.innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.BLUE);
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public boolean isPressed(double touchPosX, double touchPosY) {
        joystickCenterToTouchDistance = MathUtils.getDistance(
                outerCircleX,
                outerCircleY,
                touchPosX,
                touchPosY
        );
        return joystickCenterToTouchDistance < outerCircleRadius;
    }

    public void draw(Canvas canvas) {
        // Draw outer circle
        canvas.drawCircle(
                outerCircleX,
                outerCircleY,
                outerCircleRadius,
                outerCirclePaint
        );

        // Draw inner circle
        canvas.drawCircle(
                innerCircleX,
                innerCircleY,
                innerCircleRadius,
                innerCirclePaint
        );
    }

    public void update() {
        updateInnerCirclePosition();
    }

    private void updateInnerCirclePosition() {
        innerCircleX = (int) (outerCircleX + actuatorX * outerCircleRadius);
        innerCircleY = (int) (outerCircleY + actuatorY * outerCircleRadius);
    }

    public void setActuator(double touchPosX, double touchPosY) {
        double deltaX = touchPosX - outerCircleX;
        double deltaY = touchPosY - outerCircleY;
        double deltaDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        if (deltaDistance < outerCircleRadius) {
            actuatorX = deltaX / outerCircleRadius;
            actuatorY = deltaY / outerCircleRadius;
        } else {
            actuatorX = deltaX / deltaDistance;
            actuatorY = deltaY / deltaDistance;
        }

    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void resetActuator() {
        actuatorX = 0;
        actuatorY = 0;
    }

    public double getActuatorX() {
        return actuatorX;
    }

    public double getActuatorY() {
        return actuatorY;
    }
}
