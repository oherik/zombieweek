package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by Erik on 2015-05-28.
 */
public class ZWBody {
    private com.badlogic.gdx.physics.box2d.Body body;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;
    private Fixture fixture;

    public ZWBody(Body box2Body){
        this.body = box2Body;
        this.fixture = box2Body.getFixtureList().get(0);
    }

    public Body getBody(){
        return body;
    }

    public FixtureDef getFixtureDef(){
        return fixtureDef;
    }

    public BodyDef getBodyDef(){
        return bodyDef;
    }

    public Vector getLinearVelocity(){
        return new Vector(body.getLinearVelocity());
    }

    public float getLinearSpeed(){
        return body.getLinearVelocity().len();
    }

    public void setBody(Body body){
        this.body = body;
    }

    public void setLinearVelocity(Vector velocity){
        body.setLinearVelocity(velocity.getLibVector());
    }

    public void setAngularVelocity(float omega){
        body.setAngularVelocity(omega);
    }

    public Vector getPosition(){
        return new Vector(body.getPosition());
    }

    public float getAngle(){
        return body.getAngle();
    }

    public void setLinearDamping(float linearDampening){
        body.setLinearDamping(linearDampening);
    }

    public void setAngularDampening(float angularDampening){
        body.setAngularDamping(angularDampening);
    }

}
