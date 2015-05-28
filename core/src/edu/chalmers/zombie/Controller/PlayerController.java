package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.adapter.Player;
import edu.chalmers.zombie.adapter.Potion;
import edu.chalmers.zombie.utils.Direction;
import edu.chalmers.zombie.utils.PotionType;

/**
 * Created by neda on 2015-05-28.
 * Modified by Neda
 */
public class PlayerController {


    //TODO: move moveIfNeeded, updateDirection, updateRotation, and other controlling methods to this class.

    /**
     * A method which moves player in given direction.
     * @param player the player to be moved.
     * @param direction the direction a player will move in.
     */
    public static void move(Player player, Direction direction) {

        //TODO: fill in move.
    }

    /**
     * A method which controls the picking up of a potion, and the effects of it.
     * @param player the player picking up the potion.
     * @param potion the potion being picked up.
     */
    public static void pickUpPotion(Player player, Potion potion) {

        PotionType type = potion.getType();

        switch (type) {
            case HEALTH:
                player.incLives(50);
                break;
            case SPEED:
                //TODO: make twice as fast for approx. 10 sec.
                break;
            case SUPER_STRENGTH:
                //TODO: make twice as strong (eg. super effective attacks) for approx. 10 sec.
                break;
            case IMMUNITY:
                //TODO: make invulnerable for approx. 15 sec.
                break;
        }
    }
}
