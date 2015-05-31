package edu.chalmers.zombie.model.actors;
import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.AimingSystem;
import edu.chalmers.zombie.utils.*;
import edu.chalmers.zombie.model.CreatureInterface;
import edu.chalmers.zombie.model.Entity;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.Direction;

/**
 * Created by neda on 2015-03-31.
 * Modified by Tobias, Erik and Neda
 */
public class Player extends Entity implements CreatureInterface {

    private int killCount;
    private int lives;
    private int maxLives = 100;
    private int ammunition;
    private int grenadeAmmo = 5;
    private boolean isHidden;
    private ZWVector force;
    //Sets the player's starting direction to north so that a thrown book will have a direction.
    private Direction direction = Direction.NORTH;
    //Holds the players speed.
    private int speed = 7;
    private int legPower;
    private int waterTilesTouching;
    private int sneakTilesTouching;
    private Thread keyThread; //Keeps track of key releases
    private AimingSystem aimingSystem = new AimingSystem(this);

    private boolean isHit = false;
    private boolean diagonalStop=false; //if diagonalstop should be on/off, preferably false til bug is fixed

    public Player(PlayerType type, ZWBody body, ZWWorld world, float x, float y) {
        super(GameModel.getInstance().res.getTexture(type.getImageAnimatedKey()), world, x, y, Constants.PLAYER_SIZE);



        //Set still image frame
        addStillImage(type);

        //Set overlay image (Hand)
        setOverlayImage(type);

        setWaterTilesTouching(0);
        setSneakTilesTouching(0);

        legPower =  150; //Styr maxhastigheten
        //dampening = 30f; //Styr maxhastigheten samt hur snabb accelerationen är

        killCount = 0;
        ammunition = 5;
        lives = maxLives;
        force = new ZWVector(0,0);

        setBody(body);
        super.scaleSprite(1f / Constants.TILE_SIZE);
    }

    public void addStillImage(PlayerType type){
        ZWTexture stillTexture = GameModel.getInstance().res.getTexture(type.getImageStandingStillKey());
        ZWTextureRegion[] stillFrame = ZWTextureRegion.split(stillTexture,Constants.PLAYER_SIZE,Constants.PLAYER_SIZE);
        getAnimator().addStillFrame(stillFrame[0]);
    }

    public void setStandingStillImage(PlayerType type){
        ZWTexture stillTexture = GameModel.getInstance().res.getTexture(type.getImageStandingStillKey());
        ZWTextureRegion[] stillFrame = ZWTextureRegion.split(stillTexture,Constants.PLAYER_SIZE,Constants.PLAYER_SIZE);
        getAnimator().setStillFrame(stillFrame[0],0);
    }

    public void setOverlayImage(PlayerType type){
        ZWTexture overlayTexture = GameModel.getInstance().res.getTexture(type.getImageOverlayKey());
        ZWTextureRegion overlayFrame = new ZWTextureRegion(overlayTexture);
        getAnimator().setOverlayFrame(overlayFrame);
    }

    /**
     * A method to get current players kill count.
     * @return int killCount.
     */
    public int getKillCount() {
        return killCount;
    }

    /**
     * A method that increases current player's kill count.
     */
    public void incKillCount() {
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

    @Override
    public void knockOut() {//TODO: why is this called?

        // TODO: game over
    }

    @Override
    public boolean hasBeenAttacked() {

        return lives>100;   //TODO ändra hur detta fungerar
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

    /**
     * A method that checks whether Player is hidden or not.
     * @return true if hidden, false if not.
     */
    public boolean isHidden() {

        return isHidden;
    }

    /**
     * A method which returns the player's Hand instance.
     * @return hand instance (Hand).
     */

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

    /**
     * Decreases grenade ammunition with 1
     */
    public void decreaseGrenadeAmmunition(){
        this.grenadeAmmo = this.grenadeAmmo - 1;
    }

    public int getGrenadeAmmo(){
        return grenadeAmmo;
    }

    /**
     * Refills life
     */
    public void lifeRefill() {
        this.lives = maxLives;
    }

    public float getRadDirection(){
        return GameModel.getInstance().getAimingSystem().getDirection();
    }



}
