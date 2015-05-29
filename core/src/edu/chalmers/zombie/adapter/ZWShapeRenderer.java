package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by daniel on 5/29/2015.
 */
public class ZWShapeRenderer {
    private ShapeRenderer shapeRenderer;
    public ZWShapeRenderer(){
        shapeRenderer = new ShapeRenderer();
    }
    public void begin(){
        shapeRenderer.begin();
    }
    public void end(){
        shapeRenderer.end();
    }
    public void dispose(){
        shapeRenderer.dispose();
    }
    public void circle(float x, float y, float radius){
        shapeRenderer.circle(x, y, radius);
    }
    public void line(float x1, float y1, float x2, float y2){
        shapeRenderer.line(x1, y1, x2, y2);
    }
}
