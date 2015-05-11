package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by Erik on 2015-05-11.
 */
public class CollisionObject {
    private String property;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;

    public CollisionObject(String property, BodyDef bodyDef, FixtureDef fixtureDef){
        this.property = property;
        this.bodyDef = bodyDef;
        this.fixtureDef = fixtureDef;
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
