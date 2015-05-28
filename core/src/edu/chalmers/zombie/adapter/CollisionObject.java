package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * A class which stores a body definition, a fixture definition, the name of the collision object and, if applicable,
 * a property string.
 */
public class CollisionObject implements Cloneable {
    private String name, property;
    private ZWBody body;

    /**
     * Creates a new collision object
     * @param name  The name of the object, which is stored as a custom property for the tile in Tiled
     */
    public CollisionObject(String name, ZWBody body){
        this.name = name;
        this.body = body;
    }
    /**
     * @return The name of the collision object
     */
    public String getName(){
        return this.name;
    }

    /**
     * Sets a property if needed
     * @param property  The custom property string
     */
    public void setProperty(String property){
        this.property = property;
    }

    /**
     * @return The property
     */
    public String getProperty(){
        return this.property;
    }

    /**
     * @return The body definition
     */
    public ZWBody getBody(){
        return this.body;
    }



    /**
     * Clones the object. It's a shallow clone since when the fixture/body is created it will clone the body and fixture
     * defintion and not use the same references.
     * @return a shallow clone of the collision object, null if cloning wasn't possible
     */
    public CollisionObject clone() {
        try {
            return (CollisionObject) super.clone();
        } catch (CloneNotSupportedException e){ //If for some reason the cloning failed
            System.err.println("Cloning the collision object failed");       //TODO onödigt att ha?
            return null;
        }
    }
}
