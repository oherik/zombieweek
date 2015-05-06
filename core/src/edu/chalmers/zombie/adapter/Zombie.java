package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.model.CreatureInterface;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.Direction;
import edu.chalmers.zombie.utils.PathAlgorithm;

/**
 * Created by neda on 2015-03-31.
 */
public abstract class Zombie extends Entity implements CreatureInterface {

    private boolean isKnockedOut;
    private Vector2 force;

    /**
     * Creates a new zombie
     * @param sprite    Which sprite to use
     * @param world     In which world to create it
     * @param x     The zombie's x coordinate
     * @param y     The zombie's y coordinate
     */
    public Zombie(Sprite sprite, World world, float x, float y){

        super(sprite,world,x,y);
        int width = Constants.TILE_SIZE;
        int height = Constants.TILE_SIZE;
        //Load body def
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x+0.5f,y+0.5f);
        bodyDef.linearDamping = 20f;
        bodyDef.angularDamping = 20f;

        //Load shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/ Constants.PIXELS_PER_METER, height/2/Constants.PIXELS_PER_METER);

        //Load fixture def
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = (float)Math.pow(width/Constants.PIXELS_PER_METER, height/Constants.PIXELS_PER_METER);
        fixDef.restitution = 0;
        fixDef.friction = .8f;
        fixDef.filter.categoryBits = Constants.COLLISION_ZOMBIE;
        fixDef.filter.maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY;

        //Set body
        super.setBody(bodyDef, fixDef);
        super.getBody().setUserData(this);

        super.scaleSprite(1f / Constants.TILE_SIZE);

        isKnockedOut = false;

    }



    private boolean isAttacked;

    public abstract int getType();

    public abstract Vector2 getSpeed();

    public abstract void attack(Player player);

    public void remove(Zombie zombie) {

        //TODO: remove zombie
    }

    //@Override
    public void moveToPlayer(float x, float y, PathAlgorithm path) {


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

        return isAttacked;
    }
}
