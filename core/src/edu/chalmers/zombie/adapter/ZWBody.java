package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Erik on 2015-05-28.
 * Modified by Neda
 */
public class ZWBody {
    private com.badlogic.gdx.physics.box2d.Body body;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;
    private Fixture fixture;
    private PolygonShape shape;

    public ZWBody(){
        fixtureDef = new FixtureDef();
        bodyDef = new BodyDef();
    }

    public ZWBody(Body box2Body){
        this.body = box2Body;
        this.fixture = box2Body.getFixtureList().get(0);
        fixtureDef = new FixtureDef();
        bodyDef = new BodyDef();
    }

    public void setFixtureDef(float friction, float restitution, float boxWidth, float boxHeight, short categoryBits, short maskBits, boolean isSensor){
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(boxWidth*0.5f, boxHeight*0.5f);
        fixtureDef = new FixtureDef();
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = boxShape;
        fixtureDef.filter.maskBits = maskBits;
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.isSensor = isSensor;
    }

    public void createBodyDef(boolean dynamic, float x, float y, float linearDampening, float angularDampening){
        this.bodyDef = new BodyDef();
        if(dynamic) {
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }
        else {
            bodyDef.type = BodyDef.BodyType.StaticBody;
        }
        bodyDef.position.set(x,y);
        bodyDef.linearDamping = linearDampening;
        bodyDef.angularDamping = angularDampening;
    }
    public void createBodyDef(boolean dynamic, float x, float y, float linearDampening, float angularDampening, boolean bullet){
        this.bodyDef = new BodyDef();
        if(dynamic) {
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }
        else {
            bodyDef.type = BodyDef.BodyType.StaticBody;
        }
        bodyDef.position.set(x,y);
        bodyDef.linearDamping = linearDampening;
        bodyDef.angularDamping = angularDampening;
        bodyDef.bullet = bullet;
    }

    public void setBodyDefPosition(float x, float y){
        if(  this.bodyDef == null)
            this.bodyDef = new BodyDef();
        bodyDef.position.set(x,y);
    }


    public void setFixtureDef(float friction, float restitution,Vector[] polygonVertices, short categoryBits, short maskBits, boolean isSensor){
        Vector2[] vector2s = new Vector2[polygonVertices.length];
        for(int i = 0; i< polygonVertices.length; i++){
            vector2s[i] = new Vector2(polygonVertices[i].getLibVector());
        }
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(vector2s);

        fixtureDef = new FixtureDef();
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = polygonShape;
        fixtureDef.filter.maskBits = maskBits;
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.isSensor = isSensor;
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

    //public void setBody(Body body){
     //   this.body = body;
    //}

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

    public ZWFixture createFixture(FixtureDef fixtureDef){
        return new ZWFixture(body.createFixture(fixtureDef));
    }

    public void setBody(ZWBody body){
        this.body = body.getBody();
    }

    public void setUserData(Object obj){
        body.setUserData(obj);
    }

    public ArrayList<ZWFixture> getFixtureList(){
        ArrayList fixtures = new ArrayList<ZWFixture>();
        for(Fixture f: body.getFixtureList()){
            fixtures.add(new ZWFixture(f));
        }
        return fixtures;
    }

    public Vector getWorldCenter() {

        return new Vector(body.getWorldCenter());
    }

    public void applyLinearImpulse(Vector impulse, Vector point, boolean bool) {

        body.applyLinearImpulse(impulse.getLibVector(), point.getLibVector(), bool);
    }

    public void setAngularDamping(float angularDamping) {

        body.setAngularDamping(angularDamping);
    }

    public float getAngularVelocity() {

        return body.getAngularVelocity();
    }

    public void applyAngularImpulse(int impulse, boolean wake) {

        body.applyAngularImpulse(impulse, wake);
    }

    public void applyForceToCenter(float x, float y, boolean wake) {

        body.applyForceToCenter(x, y, wake);
    }
}
