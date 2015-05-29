package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import edu.chalmers.zombie.utils.Direction;

/**
 * Created by neda on 2015-03-31.
 */
public interface CreatureInterface {

    /**
     * A method which controls creature movement.
     * @param keyID id number of key pressed        //TODO keyID?
     */
    //void move(Direction direction);

    /**
     * A method to control a creatures attack
     * as well as whether the attack is effective
     * (registers attacks).
     */

    void knockOut();

    /**
     * A method to check whether a creature has been attacked.
     * @return true if creature has been attacked, false if not
     */
    boolean hasBeenAttacked();
}
