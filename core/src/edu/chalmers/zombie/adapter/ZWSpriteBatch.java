package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Tobias on 15-05-29.
 */
public class ZWSpriteBatch {
    private SpriteBatch spriteBatch;

    public ZWSpriteBatch(){this.spriteBatch = new SpriteBatch();}

    public void begin(){this.spriteBatch.begin();}

    public void end(){this.spriteBatch.end();}

    public SpriteBatch getSpriteBatch(){return this.spriteBatch;}

    public void dispose(){
        spriteBatch.dispose();
    }

    public void setCombinedCamera(ZWRenderer renderer){
        spriteBatch.setProjectionMatrix(renderer.getCamera().combined);
    }
}
