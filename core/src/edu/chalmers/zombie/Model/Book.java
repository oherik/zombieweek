package edu.chalmers.zombie.model;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.utils.Constants;

/**
 * Created by daniel on 4/21/2015.
 * Modified by Erik
 */
public class Book extends Entity {
    private ZWVector force;
    private float direction;
    private int speed, omega, damage;

    private long timeCreated;
    private boolean onGround;

    public Book(float x, float y, Room room){
        this(0,x,y,room.getWorld());
        speed = 0;
        ZWBody body = new ZWBody();
        body.createBodyDef(true, x+0.5f, y+0.5f, 0, 0, true);
        short maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_PLAYER | Constants.COLLISION_DOOR | Constants.COLLISION_ACTOR_OBSTACLE |
                Constants.COLLISION_LEVEL;
        body.setFixtureDef(8f, 0, 1/3f, 1/2f, Constants.COLLISION_PROJECTILE, maskBits, false);
        getBody().setUserData(this);
    }

    /**
     * Creates a book in front of the player. Since it uses vectors the speed of the book will depend on the player's speed.
     * @param d The player's direction
     * @param x The player's x position
     * @param y The player's y position
     * @param world In which world to create the physical representation of the book
     */
    public Book(float d, float x, float y, ZWWorld world) {
        super(world);

        //Set variables
        this.direction=d;
        force = new ZWVector(1,1); //if 0,0 setLength wont work

        ZWBody body = new ZWBody();
        body.createBodyDef(true,x+0.5f,y+0.5f,0,0,true);
        short categoryBits = Constants.COLLISION_PROJECTILE;
        short maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ZOMBIE | Constants.COLLISION_DOOR;
        body.setFixtureDef(8f,0,1/3f,1/2f,categoryBits,maskBits,false);

        //Set body
        super.setBody(body);
        speed = 7;
        omega= 20;  //TODO finns i entity?
        damage = 40;
        onGround = false;
        //Load sprite
        ZWSprite sprite = new ZWSprite(GameModel.getInstance().res.getTexture("book"));
        sprite.setSize(1/3f, 1/2f);
        super.setSprite(sprite);
        //setInMotion();


        //super.scaleSprite(1f / Constants.TILE_SIZE);

        getBody().setUserData(this);

        //Set system time created
        timeCreated = System.currentTimeMillis();


    }

    /**
     * @return The angular speed
     */
    public float getOmega(){
        return omega;
    }

    public boolean isOnGround(){
        return this.onGround;
    }

    public void setOnGround(boolean onGround){
        this.onGround = onGround;
    }

    /**
     * @return The damage the book makes
     */
    public int getDamage(){
        return damage;
    }

    /**
     * @return The direction the book is being thrown in
     */
    public float getDirection(){
        return direction;
    }

    /**
     * Set the book's damage
     */
        public void setDamage(int damage){
            this.damage = damage;
        }

    @Override
    public ZWVector getVelocity() {
        return getBody().getLinearVelocity();
    }

    /**
     * @return The book's speed
     * @throws NullPointerException if no body is found
     */
    public float getSpeed() throws NullPointerException{
        if(getBody()==null)
            throw new NullPointerException("getSpeed: no body found");
        return getBody().getLinearVelocity().len();
    }

    @Override
    public void setBodyVelocity(ZWVector velocity){
        super.setBodyVelocity(velocity);
    }
    @Override
    public void setAngularVelocity(float omega){
        super.setAngularVelocity(omega);
    }

    /**
     * @return  The book's throwing force
     */
    public ZWVector getForce(){
        return force;
    }

    /**
     * @return The speed the book will be thrown at
     */

    public int getThrowingSpeed(){
        return speed;
    }



    /**
     * @return  The book's mass
     * @throws  NullPointerException if no body found
     */
    public float getMass() throws NullPointerException{
        if(getBody()==null)
            throw new NullPointerException("getMass: no body found");
        return getBody().getMass();
    }

    /**
     * @return System time created
     */
    public long getTimeCreated() {
        return timeCreated;
    }
}
