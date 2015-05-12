package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by Erik on 2015-05-11.
 */
public class CollisionObject {
    private String name, property;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;

    public CollisionObject(String name, BodyDef bodyDef, FixtureDef fixtureDef){
        this.name = name;
        this.bodyDef = bodyDef;
        this.fixtureDef = fixtureDef;
    }


    public String getName(){
        return this.name;
    }

    public void setProperty(String property){
        this.property = property;
    }

    public String getProperty(){
        return this.property;
    }

    public BodyDef getBodyDef(){
        return this.bodyDef;
    }

    public FixtureDef getFixtureDef(){
        return this.fixtureDef;
    }
}
