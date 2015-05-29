package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by daniel on 5/28/2015.
 */
public class ZWTexture {
    private Texture texture;
    public ZWTexture(String filePath){
        texture = new Texture(filePath);
    }
    public ZWTexture(ZWPixmap zwPixmap){texture = new Texture(zwPixmap.getPixmap());}
    public Texture getTexture(){
        return texture;
    }

}
