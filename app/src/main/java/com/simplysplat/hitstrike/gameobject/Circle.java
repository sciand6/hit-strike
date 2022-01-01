package com.simplysplat.hitstrike.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Circle extends AI {
     protected double radius;
     protected Paint paint;

     public Circle(Context context, int color, double x, double y, double radius, String teamName) {
         super(context, x, y, teamName);
         this.radius = radius;

         paint = new Paint();
         paint.setColor(color);
     }

     public static boolean isColliding(Circle c1, Circle c2) {
         double distance = getDistanceBetweenObjects(c1, c2);
         double distanceToCollision = c1.getRadius() + c2.getRadius();
         if (distance < distanceToCollision)
             return true;
         else
             return false;
     }

     public void draw(Canvas canvas) {
         canvas.drawCircle(
                 (float) x,
                 (float) y,
                 (float) radius,
                 paint
         );
     }

     public double getRadius() {
         return radius;
     }
}
