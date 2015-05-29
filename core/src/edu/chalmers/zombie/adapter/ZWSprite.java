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

    public void setX(float x) {

        sprite.setX(x);
    }
    public void setY(float y) {

        sprite.setY(y);
    }

    public void setSize(float width, float height) {

        sprite.setSize(width, height);
    }

    public float getWidth() {

        return sprite.getWidth();
    }

    public float getHeight() {

        return sprite.getHeight();
    }

    public void setRegion(float u, float v, float u2, float v2) {

        sprite.setRegion(u, v, u2, v2);
    }

    public void setRegion(int x, int y, int width, int height) {

        sprite.setRegion(x, y, width, height);
    }
}
