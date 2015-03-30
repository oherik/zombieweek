package edu.chalmers.zombie;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Erik on 2015-03-30.
 */
public class PlayerTest extends Sprite {

    private Vector2 v;  //velocity
    private float s; //speed

    public PlayerTest(Sprite sprite){
        super(sprite);
        v = new Vector2(0,0);
        s = 64;
    }

    @Override
    public void draw(Batch batch){

    }

    public void updateLocation(float deltaTime){
        setX(getX() + deltaTime * v.x);
        setY(getY() + deltaTime * v.y);
    }


}
