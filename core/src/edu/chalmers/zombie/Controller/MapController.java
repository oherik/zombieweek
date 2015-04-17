package edu.chalmers.zombie.controller;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.model.GameModel;

/**
 * Created by Erik on 2015-04-17.
 */
public class MapController {
    GameModel gameModel;

    public MapController(GameModel gameModel){

        this.gameModel = gameModel;

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
        float tileSize = metaLayer.getTileHeight(); //kvadratiska tiles, så det  fungerar

        for(int row = 0; row < metaLayer.getHeight(); row++){       //TODO onödigt att gå igenom allt?
            for(int col = 0; col < metaLayer.getWidth(); col++){
                TiledMapTileLayer.Cell currentCell = metaLayer.getCell(col, row);       //hämta cell
                    if(currentCell != null &&
                            currentCell.getTile()!=null &&
                            currentCell.getTile().getProperties().get(collisionProperty) != null ) {     //om allt detta stämmer är det en kollisionstile
                            bodyDef.position.set((col+0.5f) / tileSize , (row+0.5f) / tileSize); // Måste ta +0.5 då de ska vara i mitten

                        ChainShape chainShape = new ChainShape();
                        Vector2[] corners = new Vector2[3];
                        corners[0] = new Vector2(-tileSize / 2, -tileSize / 2);     //nere vänster
                        corners[1] = new Vector2(-tileSize / 2, tileSize / 2);      //uppe vänster
                        corners[2] = new Vector2(tileSize / 2, tileSize / 2);       //uppe höger
                        chainShape.createChain(corners);
                        fixDef.shape = chainShape;
                        world.createBody(bodyDef).createFixture(fixDef);

                    }

            }
        }


    }

    public void createObstacles (int index){

       //TODO lägg till när tillhörande model-kod implementerats
    }
}
