package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.controller.ContactController;

/** Wrapper class for Box2d World
 * Created by Erik on 2015-05-29.
 */
public class ZWWorld {
    private World world;

    /**
     * Creates a world based on Box2d settings
     * @param gravity The world's gravity
     * @param doSleep   If the bodies should sleep to increase performanve
     */
    public ZWWorld(Vector gravity, boolean doSleep){
        world = new World(gravity.getLibVector(), doSleep);
    }

    /**
     * Creates a world with the default settings for this game (no gravity, bodies sleep)
     */
    public ZWWorld(){
        world = new World(new Vector2(0,0), true);
    }

    public void setContactListener(ContactController contactController){
        world.setContactListener(contactController);
    }

    public void step(float timeStep, int velocityIterations, int positionIterations){
        world.step(timeStep, velocityIterations, positionIterations);
    }

    public ZWBody createBody(ZWBody body){
        return new ZWBody(world.createBody(body.getBodyDef()));
    }

    public void destroyBody(ZWBody body){
        world.destroyBody(body.getBody());
    }

    public World getWorld(){
        return world;
    }
}
