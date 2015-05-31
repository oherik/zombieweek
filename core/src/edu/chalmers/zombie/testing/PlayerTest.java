package edu.chalmers.zombie.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Erik on 2015-03-30.
 */
public class PlayerTest extends Sprite {

    private Vector2 v;  //velocity

    public PlayerTest(Sprite sprite){
        super(sprite);
        v = new Vector2(0,0);
    }

    @Override
    public void draw(Batch batch){
        updateLocation(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    public void updateLocation(float deltaTime){
        setX(getX() + deltaTime * v.x);
        setY(getY() + deltaTime * v.y);
    }


}
