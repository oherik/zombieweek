package edu.chalmers.zombie.controller;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;

/**
 * Controller to handle the operations on the different maps and worlds
 * Created by Erik on 2015-04-17.
 */
public class MapController {
    GameModel gameModel;

    public MapController(){
        this.gameModel = GameModel.getInstance();
    }

    /**
     * @param levelIndex    The index in which the level is stored in the model
     * @return the world which is found at the given index
     */
    public TiledMap getMap(int levelIndex){return gameModel.getLevel(levelIndex).getMap();}

    /**
     * @return the current map from the model
     */
    public TiledMap getMap(){return gameModel.getLevel().getMap();}

    /**
     * @return the current world from the model
     */
    public World getWorld(){return gameModel.getLevel().getWorld();}

    /**
     * This function creates all the box2d obstacles. The obstacles are (at the moment, this might change in the future)
     * made up of squares one tile large each. An obstacle might be a wall, a river or anything else that the player shouldn't
     * be allowed to walk on. The maps all have a layer of meta data if they contain obstacles. A tile in the meta data layer
     * can for example have the property "collision", which states that the tile in question shouldn't be traversable. For
     * abstractation reasons the function recieves both the name of the meta layer and the collision property instead of
     * simply searching for "meta" and "collision".
     *
     * It goes through all the tiles in the map looking for tiles containing the collision property. If one is found a box2d
     * square is placed there, which allows for collision detection.
     *
     * @param metaLayerName The name of the meta layer of the map
     * @param collisionProperty The name of the collision property of the non-traversable tiles
     */

    public void createObstacles(String metaLayerName, String collisionProperty){
        World world = getWorld();
        TiledMap tiledMap = getMap();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        FixtureDef fixDef = new FixtureDef();
        fixDef.restitution = .1f;
        fixDef.friction = 0;
        TiledMapTileLayer metaLayer = (TiledMapTileLayer) tiledMap.getLayers().get(metaLayerName);
        if(metaLayer != null) {
            metaLayer.setVisible(false);
            float tileSize = Constants.TILE_SIZE;
            float ppM = Constants.PIXELS_PER_METER;

            for (int row = 0; row < metaLayer.getHeight(); row++) {       //TODO onödigt att gå igenom allt?
                for (int col = 0; col < metaLayer.getWidth(); col++) {
                    TiledMapTileLayer.Cell currentCell = metaLayer.getCell(col, row);       //hämta cell
                    if (currentCell != null &&
                            currentCell.getTile() != null &&
                            currentCell.getTile().getProperties().get(collisionProperty) != null) {     //om allt detta stämmer är det en kollisionstile
                        bodyDef.position.set((col + 0.5f) * tileSize / ppM, (row + 0.5f) * tileSize / ppM);

                        PolygonShape shape = new PolygonShape();
                        shape.setAsBox(tileSize / 2 / ppM, tileSize / 2 / ppM);
                        fixDef.shape = shape;
                        fixDef.filter.categoryBits = Constants.COLLISION_OBSTACLE;
                        fixDef.filter.maskBits = Constants.COLLISION_ENTITY;
                        world.createBody(bodyDef).createFixture(fixDef);
                    }

                }
            }
        }
    }

    public void createObstacles (int index, String metaLayerName, String collisionProperty){

       //TODO lägg till när tillhörande model-kod implementerats
    }
}
