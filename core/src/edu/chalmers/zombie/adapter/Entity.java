package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.utils.Constants;

/**
 * A class which holds a reference to a body and a sprite, as well as the world in which the body resides.
 */
public abstract class Entity {

    private Sprite sprite;
    private Body body;
    private World world;

    /**
     * Creates an entity without a sprite
     * @param world The world in which to create it
     */
    public Entity(World world){
        this.world = world;
    }

    /**
     * Creates an entity with a sprite
     * @param sprite    The sprite that will be used
     * @param world     The world in which to create it
     * @param x         The sprite's x coordinate
     * @param y         The sprite's y coordinate
     */
    public Entity(Sprite sprite, World world, float x, float y){
        this.sprite = sprite;
        sprite.setX(x);
        sprite.setY(y);
        this.world = world;
    }

    /**
     * Sets a body to use as a Box2D body
     * @param bodyDef   The body's body definition
     * @param fixDef    The body's fixture definition
     */
    public void setBody(BodyDef bodyDef, FixtureDef fixDef ){
        this.body = world.createBody(bodyDef);
        this.body.createFixture(fixDef);
    }

    /**
     * Scales the sprite
     * @param scale How the sprite should be scaled
     */
    public void scaleSprite(float scale) {
        sprite.setSize(sprite.getWidth() * scale, sprite.getHeight() * scale);
        updatePosition();
    }

    /**
     * @return The entity's body
     */
    public Body getBody() {
        return body;
    }

    /**
     * Makes the body move linearly
     * @param velocity  The body's new velocity
     */
    protected void setBodyVelocity(Vector2 velocity){
        this.body.setLinearVelocity(velocity);
    }

    /**
     * Maked the body rotate
     * @param omega The angular velocity
     */
    protected void setAngularVelocity(float omega){
        this.body.setAngularVelocity(omega);
    }

    /**
     * Draws the sprite
     * @param batch The sprite batch in which to draw it
     */
    public void draw(Batch batch){
        if(body!=null) {
            updateRotation();
            updatePosition();
        }
        sprite.draw(batch);

    }

    /**
     * Updates the sprite's rotation based on the angle of the body
     */
    private void updateRotation(){
        float angle = body.getAngle();
        float angleDegrees = angle * 180.0f / Constants.PI;
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setRotation(angleDegrees);
    }

    /**
     * Updates the sprite's position based on the position of the body.
     */
    private void updatePosition(){
        sprite.setY(body.getPosition().y - sprite.getWidth() / 2f);
        sprite.setX(body.getPosition().x - sprite.getHeight() / 2f);
    }

    /**
     * @return The body's x position
     */
    public float getX(){
        return body.getPosition().x;
    }

    /**
     * @return The body's y position
     */
    public float getY(){
        return body.getPosition().y;
    }

    /**
     * Disposes the sprite
     */
    public void dispose(){
        sprite.getTexture().dispose();
    }

    /**
     * @return The sprite's height
     */
    public float getHeight(){
        return sprite.getHeight();
    }

    /**
     * @return The sprite's width
     */
    public float getWidth(){
        return sprite.getWidth();
    }

    public abstract Vector2 getVelocity(); //TODO empty

    /**
     * Sets the entity's body
     * @param body  The new body
     */
    public void setBody(Body body) {
        this.body=body;
    }

    /**
     * @return The world in which the entity's body resides
     */
    public World getWorld(){
        return this.world;
    }

    /**
     * Sets the entity's sprite
     * @param sprite The new sprite
     */
    public void setSprite(Sprite sprite){
        this.sprite = sprite;
    }

    /**
     * Removes the body from the world in which it resides. It also sets the body to null, since it otherwise can cause
     * null pointer exceptions.
     */
    public void removeBody(){
        this.world.destroyBody(body);
        this.body = null;}
}
