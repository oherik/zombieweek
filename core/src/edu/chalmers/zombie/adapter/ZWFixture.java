package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by daniel on 5/28/2015.
 */
public class ZWFixture {
    private Fixture fixture;
    private FixtureDef fixDef;
    public ZWFixture(Fixture fixture){
        this.fixture = fixture;
    }
    public ZWFixture(Fixture fixture, FixtureDef fixDef){
        this.fixture = fixture;
        this.fixDef = fixDef;
    }
    public Fixture getFixture(){
        return fixture;
    }
    public FixtureDef getFixDef(){
        return fixDef;
    }
    public Body getBody(){
        return fixture.getBody();
    }
    public short getCategoryBits(){
        return fixture.getFilterData().categoryBits;
    }
    public void setCategoryBits(short categoryBits){
        Filter newFilter = getBody().getFixtureList().get(0).getFilterData();
        newFilter.categoryBits = categoryBits;
        fixture.setFilterData(newFilter);
    }
    public void setMaskBits(short maskBits){
        Filter newFilter = getBody().getFixtureList().get(0).getFilterData();
        newFilter.maskBits = maskBits;
        fixture.setFilterData(newFilter);
    }
    public Object getBodyUserData(){
        return fixture.getBody().getUserData();
    }
    public void setUserData(Object object){
        fixture.setUserData(object);
    }
    public Object getUserData(){
        return fixture.getUserData();
    }
    public short getMaskBits(){
        return fixture.getFilterData().maskBits;
    }
    public ZWVector getPosition(){
        return new ZWVector(getBody().getPosition());
    }
}
