package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Tobias on 15-05-29.
 */
public class ZWTextureRegion {
    private TextureRegion textureRegion;

    public ZWTextureRegion(){
        this.textureRegion = new TextureRegion();
    }

    public ZWTextureRegion(ZWTexture zwTexture){this.textureRegion.setTexture(zwTexture.getTexture());}

    public ZWTexture getZWTexture(){return new ZWTexture(this.textureRegion.getTexture());}





}
