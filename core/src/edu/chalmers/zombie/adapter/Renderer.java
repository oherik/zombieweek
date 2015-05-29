package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import edu.chalmers.zombie.model.Entity;
import edu.chalmers.zombie.model.Room;
import edu.chalmers.zombie.utils.Constants;

/**
 * A facade class holding the render instance.
 * Made as it's own class to not tangle up GameModel with LibGDX specific types, but it's otherwise a model class.
 * Created by Erik on 2015-05-24.
 */
public class Renderer {
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;

    public Renderer(Room room, float width, float height){
        mapRenderer = new OrthogonalTiledMapRenderer(room.getMap().getTiledMap(), 1f / (float) Constants.TILE_SIZE);
        camera = new OrthographicCamera(width,height);
        debugRenderer = new Box2DDebugRenderer();
    }

    public OrthogonalTiledMapRenderer getMapRenderer(){
        return mapRenderer;
    }

    public void setMapRenderer(OrthogonalTiledMapRenderer mapRenderer){
        this.mapRenderer = mapRenderer;
    }

    public void renderMapLayer(int[] layers){
         mapRenderer.render(layers);
    }

    public void renderMapLayer(){
        mapRenderer.render();
    }

    public void updateCameraPosition(float x, float y){
        camera.position.set(x, y, 0);
        camera.update();
        mapRenderer.setView(camera);
    }

    public void updateRoom(Room room, float width, float height){
        mapRenderer = new OrthogonalTiledMapRenderer(room.getMap().getTiledMap(),1f / (float) Constants.TILE_SIZE);

    }

    public ZWBatch getBatch(){
        return new ZWBatch(mapRenderer.getBatch());
    }


    public void renderBox2DDebug(Room room){
        debugRenderer.render(room.getWorld().getWorld(), camera.combined);
        camera.update();
    }

    public void drawEntity(Entity entity){
        mapRenderer.getBatch().begin();
        mapRenderer.getBatch().setProjectionMatrix(camera.combined);
        entity.draw(new ZWBatch(mapRenderer.getBatch()));
        mapRenderer.getBatch().end();
    }

}

