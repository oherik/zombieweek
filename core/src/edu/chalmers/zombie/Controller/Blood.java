package edu.chalmers.zombie.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by daniel on 5/27/2015.
 */
public class Blood {
    private Sprite blood;

    public Blood(){
        Texture bloodTexture = new Texture("core/assets/blood.png");
        blood = new Sprite(bloodTexture);
        blood.setAlpha(0.4f);
    }

    public void draw(SpriteBatch spriteBatch){
        blood.draw(spriteBatch);
    }
}
