package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by daniel on 5/28/2015.
 * Modified by Erik
 */
public class ZWBatch {
    private PolygonSpriteBatch polygonSpriteBatch;
    private Batch batch;
    public ZWBatch(Batch batch){
        polygonSpriteBatch = new PolygonSpriteBatch();
        this.batch = batch;
        polygonSpriteBatch = new PolygonSpriteBatch();
    }
    public void begin(){
        batch.begin();
        polygonSpriteBatch.begin();
    }
    public void end(){
        batch.end();
        polygonSpriteBatch.end();
    }
    public void drawPolygonRegion(ZWPolygonRegion ZWPolygonRegion, float x, float y){
        polygonSpriteBatch.draw(ZWPolygonRegion.getPolygonRegion(), x, y);
    }

    public Batch getBatch(){
        return batch;
    }
    public void dispose(){

        polygonSpriteBatch.dispose();
        batch.dispose();
    }

}
