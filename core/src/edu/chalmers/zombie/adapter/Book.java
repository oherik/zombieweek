package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.Texture;
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
    private float x;
    private float y;
    private Vector2 force;
    //Sets the player's starting direction to east so that a thrown book will have a direction.
    private Direction direction;
    private boolean remove = false;
    int speed, velocity, omega;
    float width, height;
    private long timeCreated;


    /**
     * Creates a book in front of the player. Since it uses vectors the speed of the book will depend on the player's speed.
     * @param d The player's direction
     * @param x The player's x position
     * @param y The player's y position
     * @param world In which world to create the physical representation of the book
     * @param initialSpeed  The speed which to add to the throwing speed
     */
    public Book(Direction d, float x, float y, World world, int initialSpeed) {
        super(world);
        height = Constants.TILE_SIZE/2f;
        width = Constants.TILE_SIZE/3f;

        //Set variables
        this.direction=d;
        this.speed = initialSpeed;
        force = new Vector2(0,0);
        setPosition(x, y);

        //Load body def
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(this.x+0.5f,this.y+0.5f);

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
        fixDef.filter.maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY;

        //Set body
        super.setBody(bodyDef, fixDef);
        velocity = 7;
        omega= 10;

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

    public float getX(){
        return x;
    }

    @Override
    public void setY(float y) {

    }

    public float getY(){
        return y;
    }

    @Override
    public int getSpeed() {
        return 0;
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
     * Sets the books position to be in front of the coordinates given.
     */

    public void setPosition(float x, float y){
        float distance = 1.5f;
        switch (direction) {
            case NORTH:
                y = y + distance;
                break;
            case SOUTH:
                y = y - distance;
                break;
            case WEST:
                x = x - distance;
                break;
            case EAST:
                x = x + distance;
                break;
            case NORTH_EAST:
                x =x + distance;
                y = y + distance;
                break;
            case NORTH_WEST:
                y = y + distance;
                x = x - distance;
                break;
            case SOUTH_EAST:
                x = x + distance;
                y = y - distance;
                break;
            case SOUTH_WEST:
                y = y - distance;
                x = x - distance;
                break;
            default:
                break;
        }
        this.x = x;
        this.y = y;
        setX(x);
        setY(y);
    }

    /**
     *  Starts moving the book using forces and angular rotation. The velocity of the book depends on if the player is moving and in which direction she's moving.
     */
    public void setInMotion(){
        switch(direction){
            case NORTH:
                force.y = speed + velocity;
                break;
            case SOUTH:
                force.y = -speed - velocity;
                break;
            case WEST:
                force.x = -speed - velocity;
                break;
            case EAST:
                force.x = speed + velocity;
                break;
            case NORTH_EAST:
                force.x = Constants.SQRT_2*(speed+velocity);
                force.y = Constants.SQRT_2*(speed+velocity);
                break;
            case NORTH_WEST:
                force.x = Constants.SQRT_2*(-speed-velocity);
                force.y =  Constants.SQRT_2*(speed+velocity);
                break;
            case SOUTH_EAST:
                force.x = Constants.SQRT_2*(speed+velocity);
                force.y =Constants.SQRT_2*(-speed-velocity);
                break;
            case SOUTH_WEST:
                force.x = Constants.SQRT_2*(-speed-velocity);
                force.y =  Constants.SQRT_2*(-speed-velocity);
                break;
            default:
                break;
        }
        setBodyVelocity(force);
        setAngularVelocity(omega);
    }

    @Override
    public void setX(float x) {
        //TODO empty
    }

    /**
     * Sets this book to be removed in the next world update
     */
    public void markForRemoval(){
        this.remove = true;
    }

    /**
     * @return True if the books shall be removed, false if not
     */
    public boolean toRemove(){
        return this.remove;
    }

    /**
     * @return System time created
     */
    public long getTimeCreated() {
        return timeCreated;
    }



}
