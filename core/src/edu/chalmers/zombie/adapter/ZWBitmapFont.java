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
}
