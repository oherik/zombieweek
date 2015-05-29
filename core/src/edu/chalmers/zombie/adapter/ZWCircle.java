package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.math.Circle;

/**
 * A wrapper class to libGDX's class Circle.
 * Created by neda on 2015-05-29.
 * Modified by Neda.
 */
public class ZWCircle {

    Circle circle;

    public ZWCircle(float x, float y, float radius) {

        circle = new Circle(x, y, radius);
    }

    /**
     * A method which returns the libGDX circle instance of a ZWCircle.
     * @return this.circle.
     */
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
