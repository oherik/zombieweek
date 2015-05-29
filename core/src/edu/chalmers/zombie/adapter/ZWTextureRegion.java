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

    public ZWTexture getZWTexture(){return new ZWTexture(this.textureRegion.getTexture());}

    public ZWTexture consumePixmap(){
        return  new ZWTexture(new ZWPixmap(this.textureRegion.getTexture().getTextureData().consumePixmap()));
    }



    public ZWTextureRegion(TextureRegion textureRegion){this.textureRegion=textureRegion;}

    public ZWTextureRegion(ZWTexture zwTexture){this.textureRegion.setTexture(zwTexture.getTexture());}


    public static ZWTextureRegion[] split(ZWTexture texture,int tileWidth, int tileHeight){

        TextureRegion textureRegion1[] = TextureRegion.split(texture.getTexture(),tileWidth,tileHeight)[0];

        ZWTextureRegion[] textureRegions = new ZWTextureRegion[textureRegion1.length];

        int i = 0;
        for (TextureRegion textureRegion2 : textureRegion1){
            textureRegions[i] = new ZWTextureRegion(textureRegion2);
            i++;
        }

        return textureRegions;
    }
    public TextureRegion getTextureRegion(){return this.textureRegion;}


}
