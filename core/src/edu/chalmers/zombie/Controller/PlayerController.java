package edu.chalmers.zombie.controller;

import com.badlogic.gdx.Game;
import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.actors.Player;
import edu.chalmers.zombie.model.Potion;
import edu.chalmers.zombie.model.Room;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.*;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by neda on 2015-05-28.
 * Modified by Neda and Tobias
 * Modified by Erik
 */
public class PlayerController {

    /**
     * Creates a new player and sets it to the game model
     * @param world The world
     * @param x The x coordinate
     * @param y The y coordinate
     * @return Player, the created player
     */
    public static Player createPlayer(ZWWorld world, float x, float y){
        Player player = getPlayer();
        System.out.println("Create player called");
        if(player!=null && player.getBody()!=null) {
            player.removeBody();
        }

        ZWBody body = createDefaultBody(x, y);
        player = new Player(GameModel.getInstance().getPlayerType(),body,world,x,y);
        GameModel.getInstance().setPlayer(player);

        setPositionAndFixedRotation(player,x,y);

        return player;
    }

    /**
     * Sets position and fixed rotation
     * @param player The player
     * @param x The x coordinate
     * @param y The y coordinate
     */
    private static void setPositionAndFixedRotation(Player player, float x, float y){
        setPosition(x, y);
        player.getBody().setFixedRotation(true);
    }

    /**
     * Update world and position of the player. E.g. at room change or level change.
     * @param world The world
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public static void setWorldAndPosition(ZWWorld world, float x, float y){
        System.out.println("Set world and position");

        ZWBody body = createDefaultBody(x, y);
        Player player = getPlayer();
        player.setWorld(world);
        player.setBody(body);
        setPositionAndFixedRotation(player,x,y);
    }

    /**
     * A method which moves player in given direction.
     * @param direction the direction a player will move in.
     */
    public static void move(Direction direction) {
        Player player = getPlayer();
        int legPower = player.getLegPower();
       player.setSpeed(legPower);
        switch (direction){
            case NORTH:
                player.setForceY(legPower);
                break;
            case SOUTH:
                player.setForceY(-legPower);
                break;
            case WEST:
                player.setForceX(-legPower);
                break;
            case EAST:
                player.setForceX(legPower);
                break;
            default:
                break;
        }

        updateMovement();
    }

    /**
     * Updates velocity, direction and rotation of body
     */
    private static void updateMovement(){
        updateSpeed();
        updateDirection();
        updateRotation();
        moveIfNeeded();
    }

    /**
     * Updates Body rotation
     */
    private static void updateRotation(){
        GameModel gameModel = GameModel.getInstance();
        Player player = gameModel.getPlayer();
        ZWBody body = player.getBody();
        float rotation =  player.getDirection().getRotation();
        //Checks if world.step() is running. If it is running it tries again and again until step isn't running.
        //This is the reason why sometimes the game lags and a StackOverflowError happens.
        if (!gameModel.isStepping()) {
            body.setTransform(body.getPosition(), rotation);    //TODO orsakar krash
        } else{
            EntityController.updateRotation(player);
            // Commenting the section above causes no issues and fizes the StackOverflowError.
        }
    }

    /**
     * Moves player if needed.
     */
    public static void moveIfNeeded(){
        ZWVector force = getPlayer().getForce();
        getPlayer().getBody().applyForce(force, getPlayer().getBody().getLocalCenter());
    }

    /**
     * Updates player speed
     */
    private static void updateSpeed(){getPlayer().updateSpeed();} //TODO: needed?

    /**
     * Sets Direction from variable force
     */
    private static void updateDirection(){
        Player player = getPlayer();
        ZWVector force = player.getForce();
        Direction direction = player.getDirection();
        if(force.getY() > 0){
            if (force.getX() > 0){
                direction = Direction.NORTH_EAST;
            } else if (force.getX() < 0){
                direction = Direction.NORTH_WEST;
            } else {
                direction = Direction.NORTH;
            }
        } else if (force.getY() < 0){
            if (force.getX() > 0){
                direction = Direction.SOUTH_EAST;
            } else if (force.getX() < 0){
                direction = Direction.SOUTH_WEST;
            } else {
                direction = Direction.SOUTH;
            }
        } else {
            if (force.getX() > 0){
                direction = Direction.EAST;
            } else if (force.getX() < 0){
                direction = Direction.WEST;
            }
        }
        player.setDirection(direction);
    }

    /**
     * Checks if keys are released simultaneously
     */
    private static void checkSimultaneousRelease(){
        Thread keyThread = getPlayer().getKeyThread();
        final int timeSensitiveness = 50; //release keys within x millisec and they are released simultaneously
        if (keyThread!=null && keyThread.getState() == Thread.State.TIMED_WAITING){

            //Keys were released at the same time (thread is sleeping/waiting)
            if(!GameModel.getInstance().isStepping()) {
                updateMovement();
            }
        } else {
            keyThread = new Thread() {
                public void run() {
                    try {

                        Thread thread = getPlayer().getKeyThread();
                        thread.sleep(timeSensitiveness); //waiting for new key release
                     updateMovement();
                        //if(getWorld().isLocked())     //TODO hack för att inte krascha
                    } catch (InterruptedException e) {
                        System.out.println("------ Key thread interrupted -------\n" + e);
                    }
                    //keyThread.interrupt();
                }
            };
            getPlayer().setKeyThread(keyThread);
            getPlayer().getKeyThread().start();
        }
    }

    /**
     * Checks if diagonal stop is on and checks simultaneous release of keys, else updateMovement directly
     */
    private static void updateDiagonal(){
        if (getPlayer().isDiagonalStop()) {
            checkSimultaneousRelease();
        } else {
            updateMovement();
        }
    }

    /**
     * Sets speed in x-axis to zero
     */
    public static void stopX() {
        Player player = getPlayer();
        player.setForceX(0);
        if(player.getForce().getY() == 0){player.setSpeed(0);}
        updateDiagonal();
    }

    /**
     * Sets speed in y-axis to zero
     */
    public static void stopY(){
        Player player = getPlayer();
        player.setForceY(0);
        if(player.getForce().getX() == 0){player.setSpeed(0);}
        updateDiagonal();
    }

    /**
     * Makes player throw book
     */
    public static void throwBook(){
        Player player = getPlayer();
        ProjectileController.throwBook();
        player.getAnimator().setOverlay(400); //time in millisec of Hand to be shown when trowing
    }


    /**
     * Creates a new default body at the given position
     * @param x The x coordinate where to create the new body
     * @param y The y coordinate where to create the new body
     * @return A new default body
     */
    public static ZWBody createDefaultBody(float x, float y){
           float dampening = 30f; //Styr maxhastigheten samt hur snabb accelerationen är

        ZWBody body = new ZWBody();
        body.createBodyDef(true,x,y,dampening,dampening);

        ZWVector[] vectors = new ZWVector[8];
        vectors[0] = new ZWVector(2f,-1.5f);
        vectors[1] = new ZWVector(3f,-0.5f);
        vectors[2] = new ZWVector(3f,0.5f);
        vectors[3] = new ZWVector(2f,1.5f);
        vectors[4] = new ZWVector(-2f,1.5f);
        vectors[5] = new ZWVector(-3f,0.5f);
        vectors[6] = new ZWVector(-3f,-0.5f);
        vectors[7] = new ZWVector(-2f,-1.5f);
        for (ZWVector vector:vectors){
            vector.scl(1f/6.5f);
        }


        short categoryBits = Constants.COLLISION_PLAYER;
        short maskBits = Constants.COLLISION_POTION | Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY |
                Constants.COLLISION_DOOR | Constants.COLLISION_WATER| Constants.COLLISION_SNEAK | Constants.COLLISION_ACTOR_OBSTACLE |
                Constants.COLLISION_LEVEL;

        body.setFixtureDef(.8f,0,vectors,categoryBits,maskBits,false);

        //player.setBody(body);
        //player.setPosition(x, y);
        //player.getBody().setFixedRotation(true);   //Så att spelaren inte roterar
        return body;
    }

    /**
     * A method which sets the player's position.
     * @param x desired x-coordinate.
     * @param y desired y-coordinate.
     */
    public static void setPosition(float x, float y){
        Player player = getPlayer();
        player.getBody().setTransform(x,y, player.getBody().getAngle()); //+0.5f because we want it in the middle
        updateRotation();
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
        timer.schedule(new SetSpeed(p.getLegPower() - deltaSpeed), Constants.POTION_SPEED_TIME_MILLIS);//Ej originalSpeed ifall spelaren plockat upp en till

    }
    static class SetSpeed extends TimerTask {
        private int speed;
        public SetSpeed(int speed){
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
     * Updates the player with the correct world and position and sets it in the game model.
     * @return  The updated player
     */
    public static Player updatePlayer(){
        MapController mapController = new MapController();
        GameModel gameModel = GameModel.getInstance();

        Point position = mapController.getPlayerBufferPosition();
        Player player;
        try {
            //player = new Player(type, gameModel.getRoom().getWorld(), position.x, position.y);
            player = createPlayer(gameModel.getRoom().getWorld(), position.x, position.y); //TODO: setWorldandPosition()?
        }catch (NullPointerException e){
            System.err.println("No buffered position found. Placing player at room spawn.");
            position = GameModel.getInstance().getRoom().getPlayerSpawn();
            //player = new Player(type,gameModel.getRoom().getWorld(), position.x, position.y);
            player = createPlayer(gameModel.getRoom().getWorld(), position.x, position.y); //TODO: setWorldandPosition()?
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

    public static void genderSwopIfNeeded(){
        Player player = getPlayer();

        if (player!=null) {
            PlayerType type = GameModel.getInstance().getPlayerType();
            ZWTexture texture = GameModel.getInstance().res.getTexture(type.getImageAnimatedKey());
            ZWTextureRegion[] textureRegions = ZWTextureRegion.split(texture, Constants.PLAYER_SIZE, Constants.PLAYER_SIZE);

            player.setAnimator(textureRegions, 1 / 12f);
            player.setOverlayImage(type);
            player.setStandingStillImage(type);
        }
    }
}
