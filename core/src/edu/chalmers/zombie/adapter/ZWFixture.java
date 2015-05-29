package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;

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
        fixture.getFilterData().categoryBits = categoryBits;
    }
    public void setMaskBits(short maskBits){
        fixture.getFilterData().maskBits = maskBits;
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
}
