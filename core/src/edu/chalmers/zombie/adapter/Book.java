package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.adapter.Entity;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.Direction;

/**
 * Created by daniel on 4/21/2015.
 */
public class Book extends Entity {
    private Vector2 force;
    private float direction;
    private int speed, omega, damage;
    private Vector2 initialVelocity;
    private float width, height;
    private long timeCreated;
    private boolean onGround;


    /**
     * Creates a book in front of the player. Since it uses vectors the speed of the book will depend on the player's speed.
     * @param d The player's direction
     * @param x The player's x position
     * @param y The player's y position
     * @param world In which world to create the physical representation of the book
     * @param initialVelocity  The speed which to add to the throwing speed
     */
    public Book(float d, float x, float y, World world, Vector2 initialVelocity) {
        super(world);
        height = Constants.TILE_SIZE/2f;
        width = Constants.TILE_SIZE/3f;

        //Set variables
        this.direction=d;
        this.initialVelocity = initialVelocity;
        force = new Vector2(1,1); //if 0,0 setLength wont work

        //Update position to be in front of player
        Vector2 position = getUpdatedPosition(x,y);

        //Load body def
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x+0.5f,position.y+0.5f);
        bodyDef.bullet = true;

        //Load shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/ Constants.PIXELS_PER_METER, height/2/Constants.PIXELS_PER_METER);

        //Load fixture def
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = (float)Math.pow(width/Constants.PIXELS_PER_METER, height/Constants.PIXELS_PER_METER);
        fixDef.restitution = 0;
        fixDef.friction = 8f;
        fixDef.filter.categoryBits = Constants.COLLISION_PROJECTILE;
        fixDef.filter.maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ZOMBIE | Constants.COLLISION_DOOR;

        //Set body
        super.setBody(bodyDef, fixDef);
        speed = 7;
        omega= 20;  //TODO finns i entity?
        damage = 40;
        onGround = false;

        setInMotion();

        //Load sprite
        Sprite sprite = new Sprite(new Texture("core/assets/bookSprite.png"));
        sprite.setSize(width, height);
        super.setSprite(sprite);
        super.scaleSprite(1f / Constants.TILE_SIZE);

        getBody().setUserData(this);

        //Set system time created
        timeCreated = System.currentTimeMillis();


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
     * Set the book's damage
     */
        public void setDamage(int damage){
            this.damage = damage;
        }

    @Override
    public Vector2 getVelocity() {
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
    protected void setBodyVelocity(Vector2 velocity){
        super.setBodyVelocity(velocity);
    }
    @Override
    protected void setAngularVelocity(float omega){
        super.setAngularVelocity(omega);
    }


    /**
     * Get updated position to be in front of the coordinates given.
     * @return The new position for the book
     */
    //TODO move to EntityController
    public Vector2 getUpdatedPosition(float x, float y){
        float distance = 0f;
        Vector2 position = new Vector2(x,y);
        position.y = (float)(y + distance*Math.sin(direction + Constants.PI/2));
        position.x = (float)(x + distance*Math.cos(direction + Constants.PI/2));

        return position;

    }


    /**
     *  Starts moving the book using forces and angular rotation. The velocity of the book depends on if the player is moving and in which direction she's moving.
     */
    //TODO move to EntityController
    public void setInMotion(){
        force.setLength(speed);
        force.setAngleRad(direction + Constants.PI*1/2);
        force.add(initialVelocity); // Add the player velocity
        setBodyVelocity(force);
        setAngularVelocity(omega);
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
