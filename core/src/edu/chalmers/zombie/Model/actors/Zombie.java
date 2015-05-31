package edu.chalmers.zombie.model.actors;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.controller.SpawnController;
import edu.chalmers.zombie.model.CreatureInterface;
import edu.chalmers.zombie.model.Entity;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.ZombieType;
import java.awt.*;
import java.util.ArrayList;

/**
 * A zombie is an antagonist, and will pursue and attack the player to different degrees
 * depending on its type. The detection radius, damage done, speed and starting hp varies
 * along with graphical depiction.
 *
 * Created by neda on 2015-03-31.
 * Modified by Erik, Neda
 */
public abstract class Zombie extends Entity implements CreatureInterface {

    private int speed, angularSpeed, damage, hp;
    private float radius;
    private ZombieType type;
    private boolean isKnockedOut, isMoving, isAggressive;
    private ZWVector force, point;
    private Point position, nextPathTile;
    private long timeSinceLastPath;
    private ArrayList<Point> path;

    /**
     * Creates a new zombie
     * @param walkingTexture    Which texture to use when the zombie is walking
     * @param stillTexture  Which texture to use when the zombie is standing still
     * @param deadTexture  Which texture to use when the zombie is dead
     * @param world     In which world to create it
     * @param x     The zombie's x coordinate
     * @param y     The zombie's y coordinate
     */
    public Zombie(ZWTexture walkingTexture, ZWTexture stillTexture, ZWTexture deadTexture, ZWWorld world, float x, float y, int size){
        super(walkingTexture, world, x, y, size);

        if(stillTexture == null){
            stillTexture = GameModel.getInstance().res.getTexture("zombie-still");
        }
        if(deadTexture == null)
            deadTexture = GameModel.getInstance().res.getTexture("zombie-dead");

        ZWTextureRegion[] stillFrame = ZWTextureRegion.split(stillTexture,32,32);
        getAnimator().addStillFrame(stillFrame[0]);

        ZWTextureRegion[] deadFrame = ZWTextureRegion.split(deadTexture,32,32);
        getAnimator().addStillFrame(deadFrame[0]);


        short categoryBits = Constants.COLLISION_ZOMBIE;
        short maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY | Constants.COLLISION_WATER |
                Constants.COLLISION_SNEAK | Constants.COLLISION_ACTOR_OBSTACLE | Constants.COLLISION_LEVEL;
        createBody(x,y);

        force = new ZWVector(0,0);
        point = new ZWVector(0,0);

        //Set position
        position = new Point((int)x,(int)y);

        super.scaleSprite(1f / Constants.TILE_SIZE);

        isKnockedOut = false;

        //Set system time created
        timeSinceLastPath = System.currentTimeMillis();

        isAggressive = false;
    }

    protected void createBody(float x, float y){

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

        short categoryBits = Constants.COLLISION_ZOMBIE;
        short maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY | Constants.COLLISION_WATER |
                Constants.COLLISION_SNEAK | Constants.COLLISION_ACTOR_OBSTACLE | Constants.COLLISION_LEVEL;

        ZWBody body = new ZWBody();
        body.createBodyDef(true, x+0.5f, y+0.5f, 20f, 20f);
        body.setFixtureDef(0.8f, 0, vectors, categoryBits, maskBits, false);
        //Set body
        super.setBody(body);
        super.getBody().setUserData(this);
        super.getBody().setAngularDamping(10050);
    }
    /**
     * Sets whether the zombie has been attacked or not.
     * @param isAggressive true if attacked, false if not
     */
    public void setIsAggressive(boolean isAggressive){
        this.isAggressive = isAggressive;
    }

    /**
     * A method which returns whether a zombie is agressive.
     * @return true if the zombie has been attacked, false otherwise
     */
    public boolean isAggressive(){
        return isAggressive;
    }

    /**
     * @return System time when the last path finding was performed
     */
    public long getTimeSinceLastPath() {
        return timeSinceLastPath;
    }

    /**
     * @return Set the system time when the last path finding was performed
     */
    public void setTimeSinceLastPath(long timeSinceLastPath){
        this.timeSinceLastPath = timeSinceLastPath;
    }

    /**
     * @return The current path
     */
    public ArrayList<Point> getPath() {
        return path;
    }

    /**
     * @return Set the current path
     */
    public void setPath(ArrayList<Point> path) {
        this.path = path;
    }

    /**
     * A method which sets the zombie's speed to a new speed.
     * @param newSpeed the desired new speed.
     */
    public void setSpeed(int newSpeed) {

        this.speed = newSpeed;
    }

    /**
     * A method which returns a zombie's speed.
     * @return speed (int).
     */
    public int getSpeed() {

        return speed;
    }

    /**
     * A method which sets the zombie's angular speed to a new speed.
     * @param angularSpeed The new angular speed.
     */
    public void setAngularSpeed(int angularSpeed) {

        this.angularSpeed = angularSpeed;
    }

    /**
     * A method which returns a zombie's angular speed.
     * @return angular speed (int).
     */
    public int getAngularSpeed() {

        return angularSpeed;
    }

    /**
     * A method which decreases the zombie's hp (life measured in points) by the amount stated.
     * @param decBy the amount hp will be decreased by. 
     */
    public  void decHp(int decBy) {

        hp -= decBy;
    }

    /**
     * A method which sets a zombie's starting hp.
     * @param hp the desired starting hp.
     */
    public void setStartingHp(int hp) {

        this.hp = hp;
    }

    /**
     * A method which returns a zombie's hp.
     * @return hp (int).
     */
    public int getHp(){
        return hp;
    }

    /**
     * A method which returns a zombie's type.
     * @return type (ZombieType).
     */
    public ZombieType getType() {

        return type;
    }

    /**
     * A method which sets a zombie's type.
     * @param type desired type.
     */
    public void setType(ZombieType type) {

        this.type = type;
    }

    /**
     * A method which sets a zombie's position.
     * @param pos the desired position.
     */
    public void setZombiePosition(Point pos) {

        position = pos;
    }

    /**
     * A method which returns a zombie's position.
     * @return position (Point).
     */
    public Point getZombiePosition() {

        return position;
    }

    /**
     * A method which sets force affording to chosen speed.
     * @param speed int.
     */
    public void setForceY(int speed) {

        force.setY(speed);
    }

    /**
     * A method which sets the x-line force applied to zombie.
     * @param speed desired force.
     */
    public void setForceX(int speed) {

        force.setX(speed);
    }

    /**
     * A method which sets the size of a zombie's detection span.
     * @param radius the radius of detection circle.
     */
    public void setDetectionRadius(float radius) {

        this.radius = radius;
    }

    /**
     * A method which returns the size of a zombies detection span.
     * @return radius of detection circle (float).
     */
    public float getDetectionRadius() {

        return radius;
    }

    /**
     * A method which returns the vector force applied to the zombie.
     * @return force (ZWVector):
     */
    public ZWVector getForce() {

        return force;
    }

    /**
     * A method which returns the point of applied force on zombie.
     * @return point (ZWVector).
     */
    public ZWVector getPoint() {

        return point;
    }

    /**
     * A method which sets a zombie as having been knocked out,
     * coherently changing the zombie sprite to a sleeping one.
     */
    @Override
    public void knockOut() {
        isKnockedOut = true;
    }

    /**
     * A method which returns whether or not a zombie has been knocked out.
     * @return true of knocked out, false if not.
     */
    public boolean isKnockedOut(){
        return isKnockedOut;
    }

    @Override
    public boolean hasBeenAttacked() {

        return isAggressive;
    }

    /**
     * A method which returns whether a zombie is currently moving or not.
     * @return boolean isMoving; true if moving, false if not.
     */
    public boolean isMoving() {

        return isMoving;
    }

    /**
     * A method which returns whether or not a zombie is moving.
     * @param isMoving true if moving, false if not.
     */
    public void setIsMoving(boolean isMoving) {

        this.isMoving = isMoving;
    }

    /**
     * @return  The next tile the zombie should traverse to. Add 0.5 to x and y for the center of the tile
     */
    public Point getNextPathTile(){
        return this.nextPathTile;
    }

    /**
     * Set the next tile the zombie should traverse to
     * @param nextPathTile  The next tile
     */
    public void setNextPathTile(Point nextPathTile){
        this.nextPathTile = nextPathTile;
    }

    /**
     * A method to set how much damage a zombie attack will do.
     * @param damage the amount of damage in decreased lives done.
     */
    public void setDamage(int damage) {

        this.damage = damage;
    }

    /**
     * A method to return the amount of damage a zombie attack will do.
     * @return int damage, the damage in decreased lives done.
     */
    public int getDamage() {

        return damage;
    }
}
