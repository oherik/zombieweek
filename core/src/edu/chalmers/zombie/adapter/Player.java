package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.model.CreatureInterface;
import edu.chalmers.zombie.utils.Constants;


import edu.chalmers.zombie.utils.Direction;

/**
 * Created by neda on 2015-03-31.
 * Modified by Tobias
 */
public class Player extends Entity implements CreatureInterface {

    private int killCount;
    private int lives;
    private int ammunition;
    private boolean isAttacked;
    private Body playerBody;
    private int width;
    private int height;
    private Vector2 force;
    //Sets the player's starting direction to east so that a thrown book will have a direction.
    private Direction direction = Direction.NORTH;
    //Holds the players speed.
    private int speed = 7;

    private Thread keyThread; //Keeps track of key releases


    public Player(Sprite sprite, World world, float x, float y) {
        super(sprite, world, x, y);
        width = Constants.TILE_SIZE;
        height = Constants.TILE_SIZE;

        //Load body def
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x+0.5f,y+0.5f);

        //Load shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/ Constants.PIXELS_PER_METER, height/2/Constants.PIXELS_PER_METER);
        //Load fixture def
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = (float)Math.pow(width/Constants.PIXELS_PER_METER, height/Constants.PIXELS_PER_METER);
        fixDef.restitution = 0;
        fixDef.friction = .8f;
        fixDef.filter.categoryBits = Constants.COLLISION_PLAYER;
        fixDef.filter.maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY;

        //Set body
        super.setBody(bodyDef, fixDef);
        super.scaleSprite(1f / Constants.TILE_SIZE);
        super.setSprite(sprite);
        killCount = 0;
        ammunition = 100;
        lives = 100;
        force = new Vector2(0,0);
        getBody().setFixedRotation(true);   //Så att spelaren inte roterar
    }

    private int getKillCount() {

        return killCount;
    }

    private void incKillCount() {

        killCount = killCount + 1;
    }

    @Override
    public void move(Direction direction) {
        this.speed = 7;
        switch (direction){
            case NORTH:
                force.y = speed;
                break;
            case SOUTH:
                force.y = -speed;
                break;
            case WEST:
                force.x = -speed;
                break;
            case EAST:
                force.x = speed;
                break;
            default:
                break;
        }
        updateMovement();
    }

    /**
     * Updates Body rotation
     */
    private void updateRotation(){
        Body body = getBody();
        float rotation =  direction.getRotation();
        body.setTransform(body.getPosition(), rotation);    //TODO orsakar krash
    }

    /**
     * @return Direction of player
     */
    public Direction getDirection(){
        return direction;
    }



    /**
     * Updates velocity, direction and rotation of body
     */
    private void updateMovement(){

        setBodyVelocity(force);
        updateDirecton();
        updateRotation();
    }

    /**
     * Sets Direction from variable force
     */
    private void updateDirecton(){
        if(force.y > 0){
            if (force.x > 0){
                direction = Direction.NORTH_EAST;
            } else if (force.x < 0){
                direction = Direction.NORTH_WEST;
            } else {
                direction = Direction.NORTH;
            }
        } else if (force.y < 0){
            if (force.x > 0){
                direction = Direction.SOUTH_EAST;
            } else if (force.x < 0){
                direction = Direction.SOUTH_WEST;
            } else {
                direction = Direction.SOUTH;
            }
        } else {
            if (force.x > 0){
                direction = Direction.EAST;
            } else if (force.x < 0){
                direction = Direction.WEST;
            }
        }
    }



    /**
     * Method that checks if keys are released simultaneously
     */
    private void checkSimultaneousRelease(){

        final int timeSensitiveness = 50; //release keys within x millisec and they are released simultaneously

        if (keyThread!=null && keyThread.getState() == Thread.State.TIMED_WAITING){

                //Keys were released at the same time (thread is sleeping/waiting)
                updateMovement();

        } else {

            keyThread = new Thread() {
                public void run() {
                    try {
                        keyThread.sleep(timeSensitiveness); //waiting for new key release
                        //if(getWorld().isLocked())     //TODO hack för att inte krascha
                             updateMovement(); //no more key released
                    } catch (InterruptedException e) {
                        System.out.println("------ Key thread interrupted -------\n" + e);
                    }
                    //keyThread.interrupt();

                }
            };

            keyThread.start();
        }
    }

    /**
     * Sets speed in x-axis to zero
     */
    public void stopX() {
        force.x = 0;
        if (force.y == 0) { this.speed = 0;}
        checkSimultaneousRelease();
    }

    /**
     * Sets speed in y-axis to zero
     */
    public void stopY(){
        force.y = 0;
        if (force.x == 0) { this.speed = 0;}
        checkSimultaneousRelease();
    }

    @Override
    protected void setBodyVelocity(Vector2 velocity){
        super.setBodyVelocity(velocity);
    }

    /**
     * Updates position of player
     */
    private void updatePosition(){
        //setY((float)y);
        //setX((float) x);
    }

    /**
     * Updates location of player
     */
    private void updateLocation(float deltaTime){
        setX(getX() + deltaTime * force.x);
        setY(getY() + deltaTime * force.y);
    }

    public void attack(Zombie zombie) {

        // TODO: fill in with attack of zombie instance
    }

    @Override
    public void KnockOut() {

        // TODO: game over
    }

    @Override
    public boolean hasBeenAttacked() {

        return isAttacked;
    }

    @Override
    public void setBody(Body body) {

       super.setBody(body);
    }

    @Override
    public Body getBody() {

        return super.getBody();
    }
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


    public int getSpeed(){
        return this.speed;
    }

}
