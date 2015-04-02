package edu.chalmers.zombie;

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
     * @param isAttacked (boolean)
     * @return true if creature has been attacked, false if not
     */
    boolean hasBeenAttacked();
}
