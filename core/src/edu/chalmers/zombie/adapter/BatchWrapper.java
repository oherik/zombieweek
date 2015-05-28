package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by daniel on 5/28/2015.
 */
public class BatchWrapper {
    private SpriteBatch spriteBatch;
    private PolygonSpriteBatch polygonSpriteBatch;
    public BatchWrapper(){
        spriteBatch = new SpriteBatch();
        polygonSpriteBatch = new PolygonSpriteBatch();
    }
    public void spriteBatchBegin(){
        spriteBatch.begin();
    }
    public void spriteBatchEnd(){
        spriteBatch.end();
    }
    public void polygonSpriteBatchBegin(){
        polygonSpriteBatch.begin();
    }
    public void polygonSpriteBatchEnd(){
        polygonSpriteBatch.end();
    }
    public void drawPolygonRegion(PolygonRegionWrapper polygonRegionWrapper, float x, float y){
        polygonSpriteBatch.draw(polygonRegionWrapper.getPolygonRegion(), x, y);
    }
}
