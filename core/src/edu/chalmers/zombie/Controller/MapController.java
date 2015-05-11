package edu.chalmers.zombie.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.adapter.CollisionObject;
import edu.chalmers.zombie.adapter.Level;
import edu.chalmers.zombie.adapter.Player;
import edu.chalmers.zombie.adapter.Zombie;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.testing.ZombieTest;
import edu.chalmers.zombie.utils.Constants;

import java.util.ArrayList;

/**
 * controller to handle the operations on the different maps and worlds
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
     * @return the current level from the model
     */
    public Level getLevel(){return gameModel.getLevel();}

    /**
     * Creates the different levels and stores them in the model
     */

    public void initializeLevels(){

        gameModel.addLevel(new Level("core/assets/Map/Test_world_2_previous.tmx", "core/assets/Map/Test_world_2_previous.png"));
        gameModel.addLevel(new Level("core/assets/Map/Test_world_2.tmx", "core/assets/Map/Test_world_2_bottom.png", "core/assets/Map/Test_world_2_top.png"));
        gameModel.addLevel(new Level("core/assets/Map/Test_world_2_next.tmx", "core/assets/Map/Test_world_2_next.png"));

    }

    /**
     * Scales the map images
     * @param scale The new scale
     * @throws IndexOutOfBoundsException if the scale <=0
     * @throws NullPointerException if level is null
     */
    public void scaleImage(Level level, float scale){
        if(scale<=0)
            throw new IndexOutOfBoundsException("Scale must be > 0");
        if(level == null)
            throw new NullPointerException("Level mustn't be null");
        Sprite mapPainting = level.getMapPainting();
        Sprite mapPaintingTopLayer = level.getMapPaintingTopLayer();
        mapPainting.setSize(mapPainting.getWidth() * scale, mapPainting.getHeight() * scale);
        if (mapPaintingTopLayer != null)
            mapPaintingTopLayer.setSize(mapPaintingTopLayer.getWidth() * scale, mapPaintingTopLayer.getHeight() * scale);
    }

    /**
     * Scales all the map images
     * @param scale The new scale
     * @throws IndexOutOfBoundsException if the scale <=0
     */
    public void scaleImages(float scale){
        if(scale<=0)
            throw new IndexOutOfBoundsException("Scale must be > 0");
        for(Level level : gameModel.getLevels())
            scaleImage(level, scale);
    }

    /**
     * Creates the different collision objects that represent the physical world and stores them in the model
     */
    public void initializeCollisionObjects(){
        float tileSize = Constants.TILE_SIZE;
        float ppM = Constants.PIXELS_PER_METER;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixDef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(tileSize / 2 / ppM, tileSize / 2 / ppM);

        ArrayList<CollisionObject> collisionObjects = new ArrayList<CollisionObject>();

        //Water
        fixDef.friction = 0;
        fixDef.restitution = .1f;
        fixDef.shape = shape;
        fixDef.filter.categoryBits = Constants.COLLISION_WATER;
        fixDef.filter.maskBits = Constants.COLLISION_ZOMBIE;
        collisionObjects.add(new CollisionObject("water", bodyDef, fixDef));

        //Collision for all
        fixDef = new FixtureDef();
        fixDef.friction = 0.2f;
        fixDef.restitution = .1f;
        fixDef.shape = shape;
        fixDef.filter.categoryBits = Constants.COLLISION_OBSTACLE;
        fixDef.filter.maskBits = Constants.COLLISION_ENTITY | Constants.COLLISION_PROJECTILE;
        collisionObjects.add(new CollisionObject("collision_all", bodyDef, fixDef));

        //Door, next
        fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.filter.categoryBits = Constants.COLLISION_DOOR_NEXT;
        fixDef.filter.maskBits = Constants.COLLISION_ZOMBIE | Constants.COLLISION_PROJECTILE;
        collisionObjects.add(new CollisionObject("door_next", bodyDef, fixDef));

        //Door, previous
        fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.filter.categoryBits = Constants.COLLISION_DOOR_PREVIOUS;
        fixDef.filter.maskBits = Constants.COLLISION_ZOMBIE | Constants.COLLISION_PROJECTILE;
        collisionObjects.add(new CollisionObject("door_previous", bodyDef, fixDef));

        //Sneak
        fixDef = new FixtureDef();
        fixDef.friction = 0.2f;
        fixDef.restitution = .1f;
        fixDef.shape = shape;
        fixDef.filter.categoryBits = Constants.COLLISION_SNEAK;
        fixDef.filter.maskBits = Constants.COLLISION_ZOMBIE;
        collisionObjects.add(new CollisionObject("sneak", bodyDef, fixDef));

        //Add to game model
        gameModel.setCollisionObjects(collisionObjects);

    }

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
     */

    public void createBodies(String metaLayerName, ArrayList<CollisionObject> collisionObjects) {
        Level level = getLevel();
        if(!level.hasInitializedBodies()) {
            World world = getWorld();
            TiledMap tiledMap = getMap();
            TiledMapTileLayer metaLayer = (TiledMapTileLayer) tiledMap.getLayers().get(metaLayerName);

            String zombieSpawn = "zombie_spawn"; //TODO test tills vi får flera sorters zombies
            String playerSpawn = "player_spawn"; //TODO test tills ovan är fixat

            if (metaLayer != null) {
                metaLayer.setVisible(false);
                float tileSize = Constants.TILE_SIZE;
                float ppM = Constants.PIXELS_PER_METER;
                for (int row = 0; row < metaLayer.getHeight(); row++) {       //TODO onödigt att gå igenom allt?
                    for (int col = 0; col < metaLayer.getWidth(); col++) {
                        TiledMapTileLayer.Cell currentCell = metaLayer.getCell(col, row);       //hämta cell
                        if (currentCell != null && currentCell.getTile() != null) {             //ej tom
                            for (CollisionObject obj : collisionObjects) {
                                if (currentCell.getTile().getProperties().get(obj.getProperty()) != null) {
                                    obj.getBodyDef().position.set((col + 0.5f) * tileSize / ppM, (row + 0.5f) * tileSize / ppM);
                                    world.createBody(obj.getBodyDef()).createFixture(obj.getFixtureDef());
                                }
                            }
                            if (currentCell.getTile().getProperties().get(zombieSpawn) != null) {
                                Zombie zombie = new ZombieTest(getWorld(), col, row);           //TODO test
                                getLevel().addZombie(zombie);
                            }
                            if (currentCell.getTile().getProperties().get(playerSpawn) != null) {
                                gameModel.setPlayer(new Player(new Sprite(new Texture("core/assets/player_professional_final_version.png")), getWorld(), col, row)); //TODO test
                            }
                        }
                    }
                }
            }
            level.setInitializedBodies(true);
        }
    }

    public void createBodies() {
       createBodies(gameModel.getMetaLayerName(), gameModel.getCollisionObjects());
    }


    /*  -----------------------OLD----------------------------------------
    public void createBodies(String metaLayerName, String collisionProperty){
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
                        fixDef.filter.maskBits = Constants.COLLISION_ENTITY | Constants.COLLISION_PROJECTILE;
                        world.createBody(bodyDef).createFixture(fixDef);
                    }

                }
            }
        }
    }
     -----------------------------END--------------------------------------------*/

    public void createBodies(int index, String metaLayerName, String collisionProperty){

       //TODO lägg till när tillhörande model-kod implementerats
    }

    public Level getLevel(int levelIndex){
       return gameModel.getLevel(levelIndex);
    }

    public Sprite getMapPainting(int levelIndex){
        return gameModel.getLevel(levelIndex).getMapPainting();
    }

    public Sprite getMapPaintingTopLayer(int levelIndex){
        return gameModel.getLevel(levelIndex).getMapPaintingTopLayer();
    }

    public Sprite getMapPainting(){
        return gameModel.getLevel().getMapPainting();
    }

    public Sprite getMapPaintingTopLayer(){
        return gameModel.getLevel().getMapPaintingTopLayer();
    }

    public void loadNextLevel(){
        Level level = gameModel.getNextLevel();
        createBodies();
    }

    public void loadPreviousLevel(){

    }

    public boolean worldNeedsUpdate(){
        return gameModel.worldNeedsUpdate();
    }

    public void setWorldNeedsUpdate(boolean bool){ gameModel.setWorldNeedsUpdate(bool);
    }
}
