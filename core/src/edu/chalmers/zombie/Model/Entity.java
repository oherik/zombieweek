package edu.chalmers.zombie.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.utils.Constants;

/**
 * Created by Tobias on 15-04-21.
 */
public abstract class Entity {

    private Sprite sprite;
    private Body body;
    private World world;
    private int width;
    private int height;

    public Entity(Sprite sprite, World world, float x, float y){

        width = Constants.TILE_SIZE;
        height = Constants.TILE_SIZE;

        this.sprite = sprite;
        sprite.setX(x);
        sprite.setY(y);
        sprite.setSize(width * 1/Constants.TILE_SIZE, height * 1/Constants.TILE_SIZE);

        this.world = world;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x+0.5f,y+0.5f);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/ Constants.PIXELS_PER_METER, height/2/Constants.PIXELS_PER_METER);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = (float)Math.pow(width/Constants.PIXELS_PER_METER, height/Constants.PIXELS_PER_METER);
        fixDef.restitution = .1f;
        fixDef.friction = .4f;
        fixDef.filter.categoryBits = Constants.COLLISION_PLAYER;
        fixDef.filter.maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ZOMBIE;

        body = world.createBody(bodyDef);
        body.createFixture(fixDef);


    }



    private Body getBody() {
        return body;
    }


    private void updateLocation(float deltaTime){}


    public void draw(Batch batch){
        updateRotation();
        sprite.draw(batch);
        //updateLocation(Gdx.graphics.getDeltaTime());


        //body.setTransform(getX()+0.5f,getY()+0.5f,0);
        updatePosition();

    }
    private void updateRotation(){
        float angle = body.getAngle();
        float angleDegrees = angle * 180.0f / Constants.PI;
        sprite.setOrigin(0.5f,0.5f);
        sprite.setRotation(angleDegrees);
    }

    private void updatePosition(){
        sprite.setY(body.getPosition().y-0.5f);
        sprite.setX(body.getPosition().x-0.5f);
    }


    public abstract void setX(float x);

    public float getX(){
        return body.getPosition().y;
    }

    public abstract void setY(float y);

    public float getY(){
        return body.getPosition().x;
    }



}
