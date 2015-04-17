package edu.chalmers.zombie.controller;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;

/**
 * Created by Erik on 2015-04-17.
 */
public class MapController {
    GameModel gameModel;

    public MapController(){

        this.gameModel = GameModel.getInstance();

    }

    public TiledMap getMap(int levelIndex){return gameModel.getMap(levelIndex);}
    public TiledMap getMap(){return gameModel.getMap();}
    public World getWorld(){return gameModel.getWorld();}

    public void createObstacles(String metaLayerName, String collisionProperty){
        World world = getWorld();
        TiledMap tiledMap = getMap();
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixDef = new FixtureDef();
        fixDef.restitution = .1f;
        fixDef.friction = 0;
        TiledMapTileLayer metaLayer = (TiledMapTileLayer) tiledMap.getLayers().get(metaLayerName);
        metaLayer.setVisible(false);
        float tileSize = Constants.TILE_SIZE;
        float ppM = Constants.PIXELS_PER_METER;
        short collision  = Constants.COLLISION_OBSTACLE;

        for(int row = 0; row < metaLayer.getHeight(); row++){       //TODO onödigt att gå igenom allt?
            for(int col = 0; col < metaLayer.getWidth(); col++){
                TiledMapTileLayer.Cell currentCell = metaLayer.getCell(col, row);       //hämta cell
                    if(currentCell != null &&
                            currentCell.getTile()!=null &&
                            currentCell.getTile().getProperties().get(collisionProperty) != null ) {     //om allt detta stämmer är det en kollisionstile
                            bodyDef.position.set((col + 0.5f) * tileSize /ppM , (row + 0.5f) * tileSize /ppM);

                        PolygonShape shape = new PolygonShape();
                        shape.setAsBox(tileSize / 2 / ppM, tileSize / 2 / ppM);
                        fixDef.shape = shape;
                        fixDef.filter.categoryBits = collision;
                        world.createBody(bodyDef).createFixture(fixDef);
                    }

            }
        }

        gameModel.setWorld(world);  //TODO oklart om det behövs


    }

    public void createObstacles (int index){

       //TODO lägg till när tillhörande model-kod implementerats
    }
}
