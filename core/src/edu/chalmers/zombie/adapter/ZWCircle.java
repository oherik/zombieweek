package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.math.Circle;

/**
 * A wrapper class to libGDX's class Circle.
 * Created by neda on 2015-05-29.
 * Modified by Neda.
 */
public class ZWCircle {

    Circle circle;

    /**
     * Constructor for the class ZWCircle which sends a constructor call to
     * the gdx class Circle.
     * @param x x-coordinate of circle center.
     * @param y y-coordinate of circle center.
     * @param radius the circle's radius.
     */
    public ZWCircle(float x, float y, float radius) {

        circle = new Circle(x, y, radius);
    }

    private Circle getCircleInstance() {

        return this.circle;
    }

    /**
     * A method which checks whether two circles are overlapping.
     * @param c ZWCircle.
     * @return true if overlapping, false if not.
     */
    public boolean overlaps(ZWCircle c) {

        return circle.overlaps(c.getCircleInstance());
    }
}
