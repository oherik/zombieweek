package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.utils.Constants;

/**
 * Created by daniel on 5/28/2015.
 */
public class Grenade extends Entity{
    private int targetX;
    private int targetY;
    private float width;
    private float height;
    private Texture grenadeTexture = new Texture("core/assets/grenadeBook.png");
    private Vector2 force;
    private float speed = 7;
    private float direction;
    public Grenade(int targetX, int targetY, float x, float y, World world){
        super(world);
        height = Constants.TILE_SIZE/2f;
        width = Constants.TILE_SIZE/3f;
        force = new Vector2(1,1);
        this.targetX = targetX;
        this.targetY = targetY;
        Sprite grenadeSprite = new Sprite(grenadeTexture);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x+0.5f, y+0.5f);
        bodyDef.bullet = true;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/ Constants.PIXELS_PER_METER, height/2/Constants.PIXELS_PER_METER);
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = (float)Math.pow(width/Constants.PIXELS_PER_METER, height/Constants.PIXELS_PER_METER);
        fixDef.restitution = 0;
        fixDef.friction = 0f;
        //fixDef.filter.categoryBits = Constants.COLLISION_PROJECTILE;
        fixDef.filter.maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ZOMBIE;
        super.setBody(bodyDef, fixDef);
        super.setSprite(grenadeSprite);
        super.scaleSprite(1f / Constants.TILE_SIZE);
        getBody().setUserData(this);
        calculateDirection();
        setInMotion();
    }
    private void calculateDirection(){
        float deltaX = Gdx.graphics.getWidth() / 2 - targetX;
        float deltaY = targetY - Gdx.graphics.getHeight() / 2;
        direction = (float) Math.atan2((double) deltaY, (double) deltaX) + Constants.PI / 2;
    }
    private void setInMotion(){
        force = new Vector2();
        force.setLength(speed);
        force.setAngleRad(direction + Constants.PI*1/2);
        setBodyVelocity(force);
    }
    @Override
    protected void setBodyVelocity(Vector2 velocity){
        super.setBodyVelocity(velocity);
    }
    public Vector2 getVelocity(){
        return getBody().getLinearVelocity();
    }
}
