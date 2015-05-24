package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * A facade class holding the render instance.
 * Made as it's own class to not tangle up GameModel with LibGDX specific types, but it's otherwise a model class.
 * Created by Erik on 2015-05-24.
 */
public class Renderer {
    private OrthogonalTiledMapRenderer mapRenderer;

    public OrthogonalTiledMapRenderer getMapRenderer(){
        return mapRenderer;
    }

    public void setMapRenderer(OrthogonalTiledMapRenderer mapRenderer){
        this.mapRenderer = mapRenderer;
    }
}
