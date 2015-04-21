package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Tobias on 15-04-21.
 */
public abstract class Entity {

    private Sprite sprite;
    private Body body;
    private World world;
    private int width;
    private int height;

    public Entity(Sprite sprite){
        this.sprite = sprite;
    }

    private void setBody(Body body) {
        this.body = body;
    }


    private Body getBody() {
        return body;
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
