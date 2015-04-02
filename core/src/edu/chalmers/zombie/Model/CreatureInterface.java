package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by neda on 2015-03-31.
 */
public interface CreatureInterface {

    /**
     * A method which controls creature movement.
     * @param keyID id number of key pressed
     */
    void move(int keyID);

    /**
     * A method to control a creatures attack
     * as well as whether the attack is effective
     * (registers attacks).
     */

    void KnockOut();

    /**
     * A method to check whether a creature has been attacked.
     * @return true if creature has been attacked, false if not
     */
    boolean hasBeenAttacked();

    void setGraphic(Body body);

    Body getGraphic();
}
