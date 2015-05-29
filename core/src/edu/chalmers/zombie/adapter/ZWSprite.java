package edu.chalmers.zombie.adapter;


import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by daniel on 5/28/2015.
 */
public class ZWSprite {
    private Sprite sprite;
    public ZWSprite(ZWTexture texture){
        sprite = new Sprite(texture.getTexture());
    }
    public ZWSprite(Sprite sprite){
        this.sprite = sprite;
    }
    public ZWSprite(ZWTextureRegion textureRegion){this.sprite = new Sprite(textureRegion.getTextureRegion());}

    public Sprite getSprite(){
        return sprite;
    }
    public void draw(ZWBatch batch){
        sprite.draw(batch.getSpriteBatch());
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

    public void dispose(){
        sprite.getTexture().dispose();
    }

    public void setOrigin(float x, float y){
        sprite.setOrigin(x, y);
}
    public void setRotation(float degrees){
        sprite.setRotation(degrees);
    }

    public void setRegion(ZWTextureRegion textureRegion){sprite.setRegion(textureRegion.getTextureRegion());}

}
