package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by daniel on 5/28/2015.
 */
public class ZWSprite {
    private Sprite sprite;
    public ZWSprite(Texture texture){
        sprite = new Sprite(texture);
    }
    public ZWSprite(ZWTexture texture){
        sprite = new Sprite(texture.getTexture());
    }
    public ZWSprite(Sprite sprite){
        this.sprite = sprite;
    }
    public Sprite getSprite(){
        return sprite;
    }
    public void draw(Batch batch){
        sprite.draw(batch);
    }
    public void setAlpha(float alpha){
        sprite.setAlpha(alpha);
    }
    public float getWidth(){
        return sprite.getWidth();
    }
    public float getHeight(){
        return sprite.getHeight();
    }
    public void dispose(){
        sprite.getTexture().dispose();
    }

    public void setOrigin(float x, float y){
        sprite.setOrigin(x, y);
}
    public void setRotation(float degrees){
        sprite.setRotation(degrees);
    }

    public void setY(float y){
        sprite.setY(y);
    }

    public void setX(float x){
        sprite.setX(x);
    }


}
