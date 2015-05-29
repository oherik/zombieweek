package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Wrapper class to create a font
 *
 * Created by Tobias on 15-05-29.
 */
public class ZWBitmapFont {
    private BitmapFont font;

    public ZWBitmapFont(){font = new BitmapFont();}

    /**
     * Scales font
     * @param scale The scale
     */
    public void scale(float scale){font.scale(scale);}

    public BitmapFont getBitmapFont() {
        return this.font;
    }

    public void draw(ZWSpriteBatch spriteBatch, String text, float x, float y){
        font.draw(spriteBatch.getSpriteBatch(),text,x,y);
    }

    public void setColor(float r, float g, float b, float a){
        font.setColor(r,g,b,a);
    }


}
