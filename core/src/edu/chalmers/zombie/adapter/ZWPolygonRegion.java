package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by daniel on 5/28/2015.
 */
public class ZWPolygonRegion {

    private PolygonRegion polygonRegion;

    public ZWPolygonRegion(ZWTextureRegion textureRegion, float[] vertices, short[] triangles){
        polygonRegion = new PolygonRegion(textureRegion.getTextureRegion(), vertices, triangles);
    }
    public PolygonRegion getPolygonRegion(){
        return polygonRegion;
    }
}
