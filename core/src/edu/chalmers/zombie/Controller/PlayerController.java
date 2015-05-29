package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.adapter.Player;
import edu.chalmers.zombie.adapter.Potion;
import edu.chalmers.zombie.adapter.Room;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.Direction;
import edu.chalmers.zombie.utils.PotionType;

import java.util.Timer;
import java.util.TimerTask;

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
        Room room = GameModel.getInstance().getRoom();
        switch (type) {
            case HEALTH:
                drinkHealthPotion(player);
                break;
            case SPEED:
                drinkSpeedPotion(player);
                break;
            case SUPER_STRENGTH:
                //TODO: make twice as strong (eg. super effective attacks) for approx. 10 sec.
                break;
            case IMMUNITY:
                //TODO: make invulnerable for approx. 15 sec.
                break;

        }
        room.removePotion(potion);
        GameModel.getInstance().addEntityToRemove(potion);
    }
    public static void drinkHealthPotion(Player p){
        if(p.getLives()+ Constants.POTION_HEALTH_AMOUNT>100) {
            p.incLives(100 - p.getLives());
        }
        else {
            p.incLives(Constants.POTION_HEALTH_AMOUNT);
        }
    }

    public static void drinkSpeedPotion(Player p){
        int originalSpeed = p.getLegPower();
        int newSpeed = Math.round(originalSpeed * Constants.POTION_SPEED_SCALE);
        int deltaSpeed = newSpeed-originalSpeed;
        p.setLegPower(newSpeed);
        Timer timer = new Timer();
        timer.schedule(new setSpeed(p.getLegPower() - deltaSpeed), Constants.POTION_SPEED_TIME_MILLIS);//Ej originalSpeed ifall spelaren plockat upp en till

    }
    static class setSpeed extends TimerTask {
        private int speed;
        public setSpeed(int speed){
            this.speed = speed;
        }
        public void run() {
            GameModel.getInstance().getPlayer().setLegPower(speed);
        }
    }
}
