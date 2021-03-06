package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import edu.chalmers.zombie.adapter.ZWBatch;
import edu.chalmers.zombie.controller.EntityController;
import edu.chalmers.zombie.model.Entity;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.model.Room;
import edu.chalmers.zombie.utils.Constants;

/**
 * A facade class holding the render instance.
 * Made as it's own class to not tangle up GameModel with LibGDX specific types, but it's otherwise a model class.
 * Created by Erik on 2015-05-24.
 */
public class ZWRenderer {
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private ZWBatch zwBatch;

    public ZWRenderer(){
        camera = new OrthographicCamera();
    }

    public ZWRenderer(Room room, float width, float height){
        mapRenderer = new OrthogonalTiledMapRenderer(room.getMap().getTiledMap(), 1f / (float) Constants.TILE_SIZE);
        camera = new OrthographicCamera(width,height);
        debugRenderer = new Box2DDebugRenderer();
        zwBatch = new ZWBatch(mapRenderer.getBatch());
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

    public void setCameraView(){
        mapRenderer.setView(camera);
    }

    public void setCombinedCameraBatch(){
       getBatch().getBatch().setProjectionMatrix(camera.combined);  //TODO ofint
    }

    public void renderMapLayer(){
        mapRenderer.render();
    }

    public void updateCameraPosition(float x, float y){
        camera.position.set(x, y, 0);
        camera.update();
        mapRenderer.setView(camera);
        zwBatch.setBatch(mapRenderer.getBatch());
    }

    public void updateRoom(Room room, float width, float height){
        mapRenderer = new OrthogonalTiledMapRenderer(room.getMap().getTiledMap(),1f / (float) Constants.TILE_SIZE);
        zwBatch.setBatch(mapRenderer.getBatch());
    }

    public ZWBatch getBatch(){
        return zwBatch;
    }


    public void renderBox2DDebug(ZWWorld world){
        debugRenderer.render(world.getWorld(), camera.combined);
        camera.update();
    }

    public float unprojectX(float x){
        return camera.unproject(new Vector3(x, 0, 0)).x;
    }
    public float unprojextY(float y){
        return camera.unproject(new Vector3(0, y, 0)).y;
    }

    public void resizeCamera(int width, int height){

        camera.setToOrtho(false, width / (float) Constants.TILE_SIZE, height / (float) Constants.TILE_SIZE);

    }
    public void setCameraPosition(float x, float y){
        camera.position.set(x, y, 0);
        camera.update();
    }

    public OrthographicCamera getCamera(){
        return camera;
    }


}

