package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by daniel on 5/28/2015.
 */
public class TextureWrapper {
    private Texture texture;
    public TextureWrapper(String filePath){
        texture = new Texture(filePath);
    }
    public Texture getTexture(){
        return texture;
    }
}
