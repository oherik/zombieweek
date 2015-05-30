package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by daniel on 5/28/2015.
 */
public class ZWBatch {
    private SpriteBatch spriteBatch;
    private PolygonSpriteBatch polygonSpriteBatch;
    private Batch batch;
    public ZWBatch(Batch batch){
       // spriteBatch = new SpriteBatch();
       // polygonSpriteBatch = new PolygonSpriteBatch();
        this.batch = batch;
    }
    public void begin(){
        batch.begin();
        //spriteBatch.begin();
       // polygonSpriteBatch.begin();
    }
    public void end(){
        batch.end();
      //  spriteBatch.end();
       // polygonSpriteBatch.end();
    }
    public void drawPolygonRegion(ZWPolygonRegion ZWPolygonRegion, float x, float y){
        polygonSpriteBatch.draw(ZWPolygonRegion.getPolygonRegion(), x, y);
    }
   // public SpriteBatch getSpriteBatch(){
   //     return spriteBatch;
   // }

    public Batch getBatch(){
        return batch;
    }


}
