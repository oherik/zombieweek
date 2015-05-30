package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;

/**
 * Created by Erik on 2015-05-30.
 */
public class ZWPolygonSpriteBatch {
    private PolygonSpriteBatch polygonSpriteBatch;

    public ZWPolygonSpriteBatch(){this.polygonSpriteBatch = new PolygonSpriteBatch();}

    public void begin(){this.polygonSpriteBatch.begin();}

    public void end(){this.polygonSpriteBatch.end();}

    public PolygonSpriteBatch getSpriteBatch(){return this.polygonSpriteBatch;}

    public void setCombinedCamera(ZWRenderer renderer){
        polygonSpriteBatch.setProjectionMatrix(renderer.getCamera().combined);
    }
    public void dispose(){
        polygonSpriteBatch.dispose();
    }

    public void drawPolygonRegion(ZWPolygonRegion region, float x, float y){
        polygonSpriteBatch.draw(region.getPolygonRegion(),x,y);
    }
}
