package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Animator;
import edu.chalmers.zombie.utils.Constants;

/**
 * A class which holds a reference to a body and a sprite, as well as the world in which the body resides.
 */
public abstract class Entity {

    private Sprite sprite;
    private Body body;
    private World world;
    private boolean remove = false;
    private Animator animator;
    private boolean isAnimated = false; //is entity is animated


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
        this(world);
        this.sprite = sprite;
        sprite.setX(x);
        sprite.setY(y);
    }

    public Entity(Texture texture, World world, float x, float y){
        this(world);
        animator = new Animator();
        isAnimated = true;

        TextureRegion[] textureRegions = TextureRegion.split(texture,32,32)[0];
        setAnimator(textureRegions, 1 / 12f);
        sprite = new Sprite(getAnimator().getFrame()); //gets the first frame to start with
        sprite.setSize(32,32);
        sprite.setX(x);
        sprite.setY(y);
    }


    public void setAnimator(TextureRegion[] frames, float delay){
        animator.setFrames(frames, delay);
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
    public void draw(Batch batch) {

        if (body != null) {
            updateRotation();
            updatePosition();
         }

        if(isAnimated){ //only if Entity should be animated
            if(getBody()!=null) {
                float deltaTime = 1 / (300f - getBodySpeed() * 28); //fix to get a realistic movement

                animator.update(deltaTime);

                if (getBodySpeed() < 0.2f) { //not moving
                    TextureRegion stillFrame = animator.getStillFrame();
                    if (stillFrame != null) {
                        sprite.setRegion(stillFrame);
                    } else {
                        sprite.setRegion(animator.getFrame());
                    }

                } else { //is moving
                    sprite.setRegion(animator.getFrame());
                }
            }
            else{
                TextureRegion stillFrame = animator.getStillFrame();
                if (stillFrame != null) {
                    sprite.setRegion(stillFrame);
                } else {
                    sprite.setRegion(animator.getFrame());
                }
            }
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
     * Sets the entity's sprite based on a path //TODO must throw exceptions
     * @param spritePath    The path to the sprite
     */
    public void setSprite(String spritePath){
        this.sprite = new Sprite(new Texture(spritePath));
    }
    /**
     * Removes the body from the world in which it resides. It also sets the body to null, since it otherwise can cause
     * null pointer exceptions.
     */
    public void removeBody(){
        this.world.destroyBody(body);
        this.body = null;}

    public void nullifyBody(){
        this.body = null;
    }

    /**
     * Sets this entity to be removed in the next world update
     */
    public void markForRemoval(){
        this.remove = true;
    }

    /**
     * @return True if the entity shall be removed, false if not
     */
    public boolean toRemove(){
        return this.remove;
    }

    /**
     * Sets the current player world
     * @param world The current world
     */
    public void setWorld(World world){
        this.world=world;
    }

    public Animator getAnimator(){return animator;}

    public Sprite getSprite(){return sprite;}

    /**
     * Sets the entity's category bits, used for collision detection
     * @param bits  The category bits
     */
    public void setCategoryBits(short bits) throws NullPointerException{
        Filter newFilter = getBody().getFixtureList().get(0).getFilterData();
        newFilter.categoryBits = bits;
        getBody().getFixtureList().get(0).setFilterData(newFilter);
    }

    /**
     * Sets the entity's mask bits, used for collision detection
     * @param bits  The mask bits
     */
    public void setMaskBits(short bits) throws NullPointerException{
        Filter newFilter = getBody().getFixtureList().get(0).getFilterData();
        newFilter.maskBits = bits;
        getBody().getFixtureList().get(0).setFilterData(newFilter);
    }
    
    public float getBodySpeed(){
        try {
        return body.getLinearVelocity().len();
        } catch (NullPointerException e) {
            System.err.println("Get body speed: no body found");
            return 0f;
        }
    }

}
