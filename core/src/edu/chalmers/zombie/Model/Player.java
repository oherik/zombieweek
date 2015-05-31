package edu.chalmers.zombie.model;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.Direction;
import edu.chalmers.zombie.utils.PotionType;

import java.awt.*;

/**
 * Created by neda on 2015-03-31.
 * Modified by Tobias and Erik
 */
public class Player extends Entity implements CreatureInterface {

    private int killCount;
    private int lives;
    private int ammunition;
    private int grenadeAmmo = 5;
    private boolean isAttacked;
    private boolean isHidden;
    private int width;
    private int height;
    private ZWVector force;
    //Sets the player's starting direction to north so that a thrown book will have a direction.
    private Direction direction = Direction.NORTH;
    //Holds the players speed.
    private int speed = 7;
    private float dampening;
    private int legPower;
    private PotionType potion;
    private int waterTilesTouching = 0; //TODO måste göras på nåt snyggare sätt
    private int sneakTilesTouching = 0; //TODO måste göras på nåt snyggare sätt

    private Thread keyThread; //Keeps track of key releases
    //The hand is throwing the book and aiming.
    private Hand hand = new Hand(this);

    private boolean isHit = false;
    private boolean diagonalStop=false; //if diagonalstop should be on/off, preferably false til bug is fixed


    public Player(ZWTexture texture, ZWWorld world, float x, float y) {
        super(texture, world, x, y);

        //Set still image frame
        //GameModel.getInstance().res.loadTexture("emilia-still","core/assets/Images/emilia-still.png"); //TODO: shouldnt be done here
        ZWTexture stillTexture = GameModel.getInstance().res.getTexture("emilia-still");
        ZWTextureRegion[] stillFrame = ZWTextureRegion.split(stillTexture,32,32);
        getAnimator().addStillFrame(stillFrame[0]);

        //Set overlay image (Hand)
        //GameModel.getInstance().res.loadTexture("emilia-hand","core/assets/Images/emilia-hand.png");//TODO: shouldnt be done here
        ZWTexture overlayTexture = GameModel.getInstance().res.getTexture("emilia-hand");
        System.out.println(overlayTexture.getTexture());
        ZWTextureRegion overlayFrame = new ZWTextureRegion(overlayTexture);
        getAnimator().setOverlayFrame(overlayFrame);


        legPower =  150; //Styr maxhastigheten
        dampening = 30f; //Styr maxhastigheten samt hur snabb accelerationen är

        width = Constants.PLAYER_SIZE;
        height = Constants.PLAYER_SIZE;

        createDefaultBody(world,x,y);

        super.scaleSprite(1f / Constants.TILE_SIZE);
        killCount = 0;
        ammunition = 5;
        lives = 100;
        force = new ZWVector(0,0);


    }

    /**
     * Copy constructor for class Player.
     * @param p the Player instance to be copied.
     */
    public Player(Player p) {
        this(p.getSprite().getTexture(), p.getWorld(), p.getX(), p.getY());
    }

    /**
     * A method to get current players kill count.
     * @return int killCount.
     */
    private int getKillCount() {

        return killCount;
    }

    /**
     * A method that increases current player's kill count.
     */
    private void incKillCount() {

        killCount++;
    }

    /**
     * A method which sets the player's leg power.
     * @param legPower desired leg power.
     */
    public void setLegPower(int  legPower){
        this.legPower = legPower;
        speed = legPower;
        updateSpeed();
    }

    /**
     * A method which returns the player's leg power.
     * @return leg power (int).
     */
    public int getLegPower(){
        return legPower;
    }

    public void setForceY(float speed){force.setY(speed);}

    public void setForceX(float speed){force.setX(speed);}

    public void setSpeed(int speed){this.speed = speed;}

    /**
     * Updates Body rotation
     * TODO: should be removed
     */
    public void updateRotation(){
        GameModel gameModel = GameModel.getInstance();
        ZWBody body = getBody();
        float rotation =  direction.getRotation();
        //Checks if world.step() is running. If it is running it tries again and again until step isn't running.
        //This is the reason why sometimes the game lags and a StackOverflowError happens.
        if (!gameModel.isStepping()) {
            body.setTransform(body.getPosition(), rotation);    //TODO orsakar krash
        } else{
            updateRotation();
            // Commenting the section above causes no issues and fizes the StackOverflowError.
        }
    }

    /**
     * @return Direction of player
     */
    public Direction getDirection(){
        return direction;
    }

    /**
     * Updates player speed
     */
    public void updateSpeed(){force.setLength(speed);}

    /**
     * A method which returns the player's speed.
     * @return speed (float).
     */
    public float getSpeed(){return this.speed;}

    public void setForceLength(float speed){force.setLength(speed);}

    public ZWVector getForce(){return this.force;}

    /**
     * A method which sets the player's current direction.
     * @param direction
     */
    public void setDirection(Direction direction){this.direction = direction;}

    public Thread getKeyThread(){return this.keyThread;}

    public void setKeyThread(Thread keyThread){this.keyThread = keyThread;}

    public boolean isDiagonalStop(){return this.diagonalStop;}

    @Override
    public void setBodyVelocity(ZWVector velocity){
        super.setBodyVelocity(velocity);
    }

    /**
     * A method used when player is attacking zombie.
     * @param zombie the zombie that is attacked.
     */
    public void attack(Zombie zombie) {

        // TODO: fill in with attack of zombie instance
    }

    @Override
    public void knockOut() {

        // TODO: game over
    }

    @Override
    public boolean hasBeenAttacked() {

        return isAttacked;
    }

    @Override
    public void setBody(ZWBody body) {

       super.setBody(body);
    }

    @Override
    public ZWBody getBody() {

        return super.getBody();
    }

    /**
     * A method that increases ammuntion.
     */
    public void addBook(){
        ++ammunition;
    }

    /**
     * Since the ammunition count can't be negative it checks if it's >0 before decreasing it
     */
    public void decreaseAmmunition(){
        if(ammunition>0)
            ammunition--;
    }

    /**
     * A method which increases the player's ammunition by one.
     */
    public void increaseAmmunition(){
        ammunition++;
    }

    /**
     * Get player ammunition
     * @return ammunition
     */
    public int getAmmunition(){
        return ammunition;
    }

    /**
     * Get player lives
     * @return lives
     */
    public int getLives(){
        return lives;
    }

    /**
     * A method which decreases the player's number of lives by the amount stated.
     * @param decBy the amount by which the player's lives will decrease. 
     */
    public void decLives(int decBy) {

        lives -= decBy;
    }

    /**
     * A method which increases the player's number of lives by the amount stated.
     * @param incBy the amount by which the player's lives is to be increased.
     */
    public void incLives(int incBy) {

        lives += incBy;
    }

    /**
     * A method which returns the player's velocity.
     * @return velocity (ZWVector).
     */
    public ZWVector getVelocity(){
        return getBody().getLinearVelocity(); //TODO måste fixas, borde skicka en vector2
    }

    private void setDefaultBody(){
        if(this.getBody()!=null) {
            this.removeBody();
        }


    }

    /**
     * Creates a new default body at the given position
     * @param x The x coordinate where to create the new body
     * @param y The y coordinate where to create the new body
     * @return A new default body
     */
    public ZWBody createDefaultBody(ZWWorld world, float x, float y){
        if(this.getBody()!=null) {
            this.removeBody();
        }
        this.setWorld(world);
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
                Constants.COLLISION_DOOR | Constants.COLLISION_WATER| Constants.COLLISION_SNEAK | Constants.COLLISION_ACTOR_OBSTACLE;

        body.setFixtureDef(.8f,0,vectors,categoryBits,maskBits,false);

        this.setBody(body);
        this.setPosition(x,y);
        getBody().setFixedRotation(true);   //Så att spelaren inte roterar
        return this.getBody();
    }


    /**
     * A method that checks whether Player is hidden or not.
     * @return true if hidden, false if not.
     */
    public boolean isHidden() {

        return isHidden;
    }

    /**
     * A method which sets the player's position.
     * @param x desired x-coordinate.
     * @param y desired y-coordinate.
     */
    public void setPosition(float x, float y){
        getBody().setTransform(x,y, getBody().getAngle()); //+0.5f because we want it in the middle
        updateRotation();
        //TODO: should be done in controller, method updateRotation will be removed
    }

    /**
     * A method which returns the player's Hand instance.
     * @return hand instance (Hand).
     */
    public Hand getHand(){
        return this.hand;
    }

    //TODO : move to PlayerController
    public void throwBook(){
        hand.throwBook();
        getAnimator().setOverlay(500); //time in millisec of Hand to be shown when trowing
    }

    //TODO: move to PlayerController
    public void throwGrenade(){
        hand.throwGrenade();
    }

    public int getWaterTilesTouching(){
        return waterTilesTouching;
    }

    public void setWaterTilesTouching(int i){
        waterTilesTouching = i;
    }

    public int getSneakTilesTouching(){
        return sneakTilesTouching;
    }

    public void setSneakTilesTouching(int i){
        sneakTilesTouching = i;
    }

    /**
     * A method which sets whether or not the player is hidden.
     * @param isHidden true if hidden, false if not.
     */
    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    /**
     * A method which sets whether or not the player has been hit.
     * @param isHit true if hit, false if not.
     */
    public void setIsHit(boolean isHit){
        this.isHit = isHit;
    }

    /**
     * A method which returns whether or not the player has been hit.
     * @return true if hit, false if not. 
     */
    public boolean isHit(){
        return isHit;
    }
}
