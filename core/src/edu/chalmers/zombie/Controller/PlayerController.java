package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.model.Player;
import edu.chalmers.zombie.model.Potion;
import edu.chalmers.zombie.model.Room;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.Direction;
import edu.chalmers.zombie.utils.PotionType;
import edu.chalmers.zombie.utils.ResourceManager;

import java.awt.*;
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
        GameModel.getInstance().addEntityToRemove(GameModel.getInstance().getRoom(),potion);
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

    /**
     * @return  The current player
     */
    public static Player getPlayer(){
        return GameModel.getInstance().getPlayer();
    }
    /**
     * Sets the current player
     * @param player The new player
     */
    public static void setPlayer(Player player){
        GameModel.getInstance().setPlayer(player);
    }

    /**
     * Creates a new player and sets it in the game model.
     * @return  The newly created player
     */
    public static Player createNewPlayer(){
        MapController mapController = new MapController();

        GameModel gameModel = GameModel.getInstance();
        ResourceManager res = gameModel.res;

        Point position = mapController.getPlayerBufferPosition();
        Player player;
        try {
            player = new Player(res.getTexture("emilia"), gameModel.getRoom().getWorld(), position.x, position.y);
        }catch (NullPointerException e){
            System.err.println("No buffered position found. Placing player at room spawn.");
            position = GameModel.getInstance().getRoom().getPlayerSpawn();
            player = new Player(res.getTexture("emilia"),gameModel.getRoom().getWorld(), position.x, position.y);
        }
        setPlayer(player); //TODO test);
        return player;
    }

    /**
     * Increse the number of sneak tiles the player is touching by one. The player will start to sneak.
     * @param player    The player
     */
    public static void increaseSneakTilesTouching(Player player){
        if(player.getSneakTilesTouching()<0) {
            player.setSneakTilesTouching(0);        //Negative numbers for tiles youching wouldn't make sense
        }
        player.setSneakTilesTouching(player.getSneakTilesTouching() + 1);
        //TODO add more sneak stuff
        player.setHidden(true); //TODO N�dv�ndigt? Zombien kommer �nd� inte kunna hitta till spelaren
        EntityController.setFriction(player, Constants.PLAYER_FRICTION_SNEAK, Constants.PLAYER_FRICTION_SNEAK);  //TODO on�digt att g�ra varje g�ng?
    }

    /**
     * Decrease the number of sneak tiles the player is touching by one. The player will stop to sneak if the total number of sneak tiles it's touching is 0.
     * @param player The player
     */
    public static void decreaseSneakTilesTouching(Player player){
        player.setSneakTilesTouching(player.getSneakTilesTouching() - 1);
        if(player.getSneakTilesTouching()<1){
            //TODO Sluta sneaka
            player.setHidden(false);
            EntityController.setFriction(player, Constants.PLAYER_FRICTION_DEFAULT, Constants.PLAYER_FRICTION_DEFAULT);
            if(player.getSneakTilesTouching()<0) {
                player.setSneakTilesTouching(0);        //Negative numbers for tiles touching wouldn't make sense
            }
        }
    }

    /**
     * Increse the number of water tiles the player is touching by one. The player will slow down.
     * @param player    The player
     */
    public static void increaseWaterTilesTouching(Player player){
        if(player.getWaterTilesTouching()<0) {
            player.setWaterTilesTouching(0);
        }
        player.setWaterTilesTouching(player.getWaterTilesTouching() + 1);
        //TODO add water stuff
        EntityController.setFriction(player, Constants.PLAYER_FRICTION_WATER, Constants.PLAYER_FRICTION_WATER);
    }

    /**
     * Decrease the number of water tiles the player is touching by one. The player will get out of the water if the total number of water tiles it's touching is 0.
     * @param player The player
     */
    public static void decreaseWaterTilesTouching(Player player){
        player.setWaterTilesTouching(player.getWaterTilesTouching() - 1);
        if(player.getWaterTilesTouching()<1){
            EntityController.setFriction(player, Constants.PLAYER_FRICTION_DEFAULT, Constants.PLAYER_FRICTION_DEFAULT);
            if(player.getWaterTilesTouching()<0) {
                player.setWaterTilesTouching(0);
            }
        }
    }
}
