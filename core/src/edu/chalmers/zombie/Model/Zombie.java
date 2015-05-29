package edu.chalmers.zombie.model;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.controller.MapController;
import edu.chalmers.zombie.controller.SpawnController;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.ZombieType;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by neda on 2015-03-31.
 */
public abstract class Zombie extends Entity implements CreatureInterface {

    private int speed, angularSpeed, damage, hp;
    private float radius;
    private ZombieType type;
    private boolean isKnockedOut, isMoving, isAggressive;
    private ZWVector force, point;
    private Point position, nextPathTile;
    private MapController mapController;
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
    public Zombie(ZWTexture walkingTexture, ZWTexture stillTexture, ZWTexture deadTexture, ZWWorld world, float x, float y){
        super(walkingTexture, world, x, y);

        if(walkingTexture == null) {
            walkingTexture = GameModel.getInstance().res.getTexture("zombie");
            setSprite(new ZWSprite(walkingTexture));
        }
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
                Constants.COLLISION_SNEAK | Constants.COLLISION_ACTOR_OBSTACLE;

        ZWBody body = new ZWBody();
        body.createBodyDef(true, x+0.5f, y+0.5f, 20f, 20f);
        body.setFixtureDef(0.8f, 0, 1, 1, categoryBits, maskBits, false);

        //Set vectors
        force = new ZWVector(0,0);
        point = new ZWVector(0,0);

        //Set position
        position = new Point((int)x,(int)y);
        
        //Set body
        super.setBody(body);
                super.getBody().setUserData(this);

        super.scaleSprite(1f / Constants.TILE_SIZE);

        mapController = new MapController();
        SpawnController.setCollisionObjects();
        mapController.setPlayerBufferPosition(GameModel.getInstance().getRoom().getPlayerSpawn());

        super.getBody().setAngularDamping(10000);

        isKnockedOut = false;

        //Set system time created
        timeSinceLastPath = System.currentTimeMillis();

        isAggressive = false;
    }

    /**
     * Set the zombie as attacked or not
     * @param isAggressive    true if attacked, false if not
     */
    public void setIsAggressive(boolean isAggressive){
        this.isAggressive = isAggressive;
    }

    /**
     * @return true if the zombie is attacked, false otherwise
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
     * @param newSpeed
     */
    public void setSpeed(int newSpeed) {

        this.speed = newSpeed;
    }

    public int getSpeed() {

        return speed;
    }

    /**
     * A method which sets the zombie's angular speed to a new speed.
     * @param angularSpeed The new angular speed
     */
    public void setAngularSpeed(int angularSpeed) {

        this.angularSpeed = angularSpeed;
    }

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

    public void setStartingHp(int hp) {

        this.hp = hp;
    }

    public int getHP(){
        return hp;
    }

    public ZombieType getType() {

        return type;
    }

    public void setType(ZombieType type) {

        this.type = type;
    }

    public void setZombiePosition(Point pos) {

        position = pos;
    }

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

    public void setForceX(int speed) {

        force.setX(speed);
    }

    public void setDetectionRadius(float radius) {

        this.radius = radius;
    }

    public float getDetectionRadius() {

        return radius;
    }

    public ZWVector getForce() {

        return force;
    }

    public ZWVector getPoint() {

        return point;
    }

    /**
     * Changes the sprite to a sleeping one
     */
    @Override
    public void knockOut() {
        isKnockedOut = true;
    }

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

    public void setIsMoving(boolean isMoving) {

        this.isMoving = isMoving;
    }

    public MapController getThisMapController() {

        return mapController;
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
