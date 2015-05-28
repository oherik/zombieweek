package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by daniel on 5/28/2015.
 */
public class FixtureWrapper {
    private Fixture fixture;
    private FixtureDef fixDef;
    public FixtureWrapper(Fixture fixture, FixtureDef fixDef){
        this.fixture = fixture;
        this.fixDef = fixDef;
    }
    public Fixture getFixture(){
        return fixture;
    }
    public FixtureDef getFixDef(){
        return fixDef;
    }
}
