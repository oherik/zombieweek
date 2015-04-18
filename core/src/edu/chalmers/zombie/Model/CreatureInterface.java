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
    void move(Direction direction);

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

    /**
     * A method which sets the physical representation of the creature.
     * @param body Body
     */
    void setBody(Body body);

    /**
     * A method which returns the physical representation of the creature.
     * @return Body
     */
    Body getBody();

    /**
     * A method which sets the graphical representation of the creature.
     * @param sprite Sprite
     */
    void setSprite(Sprite sprite);

    /**
     * A method which returns the graphical representation of the creature.
     * @return Sprite
     */
    Body getSprite();
}
