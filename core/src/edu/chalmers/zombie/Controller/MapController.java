package edu.chalmers.zombie.controller;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.adapter.CollisionObject;
import edu.chalmers.zombie.adapter.Level;
import edu.chalmers.zombie.adapter.Player;
import edu.chalmers.zombie.adapter.Zombie;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.testing.ZombieTest;
import edu.chalmers.zombie.utils.Constants;

import java.awt.*;
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

        gameModel.addLevel(new Level("core/assets/Map/Test_world_2_previous.tmx"));
        gameModel.addLevel(new Level("core/assets/Map/Test_world_3.tmx"));
        gameModel.addLevel(new Level("core/assets/Map/Test_world_2_next.tmx"));

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

        PolygonShape standardBoxShape = new PolygonShape();
        standardBoxShape.setAsBox(tileSize / 2 / ppM, tileSize / 2 / ppM);
        PolygonShape doorShape = new PolygonShape();
        doorShape.setAsBox(tileSize / 4 / ppM, tileSize / 2 / ppM);

        ArrayList<CollisionObject> collisionObjects = new ArrayList<CollisionObject>();

        //Water
        fixDef.friction = 0;
        fixDef.restitution = .1f;
        fixDef.shape = standardBoxShape;
        fixDef.filter.categoryBits = Constants.COLLISION_WATER;
        fixDef.filter.maskBits = Constants.COLLISION_ZOMBIE;
        collisionObjects.add(new CollisionObject("water", bodyDef, fixDef));

        //Collision for all
        fixDef = new FixtureDef();
        fixDef.friction = 0.2f;
        fixDef.restitution = .1f;
        fixDef.shape = standardBoxShape;
        fixDef.filter.categoryBits = Constants.COLLISION_OBSTACLE;
        fixDef.filter.maskBits = Constants.COLLISION_ENTITY | Constants.COLLISION_PROJECTILE;
        collisionObjects.add(new CollisionObject(Constants.COLLISION_PROPERTY_ALL, bodyDef, fixDef));

        //Door
        fixDef = new FixtureDef();;
        fixDef.shape = doorShape;
        fixDef.filter.categoryBits = Constants.COLLISION_OBSTACLE;
        fixDef.filter.maskBits = Constants.COLLISION_ENTITY | Constants.COLLISION_PROJECTILE;
        collisionObjects.add(new CollisionObject(Constants.DOOR_PROPERTY, bodyDef, fixDef));

        //Sneak
        fixDef = new FixtureDef();
        fixDef.friction = 0.2f;
        fixDef.restitution = .1f;
        fixDef.shape = standardBoxShape;
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

    public void createBodiesIfNeeded(String metaLayerName, ArrayList<CollisionObject> collisionObjects) {
        Level level = getLevel();
        if(!level.hasInitializedBodies()) {
            World world = getWorld();
            TiledMap tiledMap = getMap();
            TiledMapTileLayer metaLayer = (TiledMapTileLayer) tiledMap.getLayers().get(metaLayerName);

            String zombieSpawn = "zombie_spawn"; //TODO test tills vi får flera sorters zombies
            String playerSpawn = "player_spawn"; //TODO test tills ovan är fixat
            String playerReturn = "player_return"; //TODO test tills ovan är fixat

            if (metaLayer != null) {
                metaLayer.setVisible(false);
                float tileSize = Constants.TILE_SIZE;
                float ppM = Constants.PIXELS_PER_METER;
                for (int row = 0; row < metaLayer.getHeight(); row++) {       //TODO onödigt att gå igenom allt?
                    for (int col = 0; col < metaLayer.getWidth(); col++) {
                        TiledMapTileLayer.Cell currentCell = metaLayer.getCell(col, row);       //hämta cell
                        if (currentCell != null && currentCell.getTile() != null) {             //ej tom
                            for (CollisionObject obj : collisionObjects) {
                                if (currentCell.getTile().getProperties().get(obj.getName()) != null) {
                                    obj.getBodyDef().position.set((col + 0.5f) * tileSize / ppM, (row + 0.5f) * tileSize / ppM);
                                    Fixture fixture = world.createBody(obj.getBodyDef()).createFixture(obj.getFixtureDef());
                                    if(obj.getName().equals(Constants.DOOR_PROPERTY)){
                                        obj.setProperty((String) currentCell.getTile().getProperties().get(Constants.DOOR_PROPERTY));       //Dvs leveln den leder till
                                    }
                                    fixture.getBody().setUserData(obj.getName());
                                }
                            }
                            if (currentCell.getTile().getProperties().get(zombieSpawn) != null) {
                                Zombie zombie = new ZombieTest(getWorld(), col, row);           //TODO test
                                getLevel().addZombie(zombie);
                            }
                            if (currentCell.getTile().getProperties().get(playerSpawn) != null) {
                                level.setPlayerSpawn(new Point(col, row));
                                gameModel.setPlayer(new Player(new Sprite(gameModel.res.getTexture("player")), getWorld(), col, row)); //TODO test
                            }
                            if (currentCell.getTile().getProperties().get(playerReturn) != null) {
                                level.setPlayerReturn(new Point(col, row));
                            }
                        }
                    }
                }
            }
            level.setInitializedBodies(true);
        }
    }

    public void createBodiesIfNeeded() {
       createBodiesIfNeeded(gameModel.getMetaLayerName(), gameModel.getCollisionObjects());
    }


    /*  -----------------------OLD----------------------------------------
    public void createBodiesIfNeeded(String metaLayerName, String collisionProperty){
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

    public void createBodiesIfNeeded(int index, String metaLayerName, String collisionProperty){

       //TODO lägg till när tillhörande model-kod implementerats
    }

    public Level getLevel(int levelIndex){
       return gameModel.getLevel(levelIndex);
    }

    public Sprite getMapBottomLayer(int levelIndex){
        return gameModel.getLevel(levelIndex).getMapPainting();
    }

    public Sprite getMapTopLayer(int levelIndex){
        return gameModel.getLevel(levelIndex).getMapPaintingTopLayer();
    }

    public TiledMapImageLayer getMapBottomLayer(){
        return gameModel.getLevel().getBottomLayer();
    }

    public TiledMapImageLayer getMapTopLayer(){
        return gameModel.getLevel().getTopLayer();
    }

    public TiledMapTileLayer getMapMetaLayer(){
        return gameModel.getLevel().getMetaLayer();
    }
    /**
     * Loads the next level in the game model, creates bodies if needed and sets that the renderer needs to update the world
     * @throws  IndexOutOfBoundsException if the current level is the last
     */
    public void loadNextLevel(){
        int currentLevel = gameModel.getCurrentLevelIndex();
        if(currentLevel ==gameModel.getLevels().size()-1)
            throw new IndexOutOfBoundsException("GameModel: already at last indexed level");
        currentLevel+=1;
        gameModel.setCurrentLevelIndex(currentLevel);
        createBodiesIfNeeded();
        setPlayerBufferPosition(getLevel().getPlayerSpawn());
        gameModel.setWorldNeedsUpdate(true);
    }

    /**
     * Loads the previous level in the game model, creates bodies if needed and sets that the renderer needs to update the world
     * @throws  IndexOutOfBoundsException if the current level is the first
     */
    public void loadPreviousLevel(){
        int currentLevel = gameModel.getCurrentLevelIndex();
        if(currentLevel == 0)
            throw new IndexOutOfBoundsException("GameModel: already at first indexed level");
        currentLevel-=1;
        gameModel.setCurrentLevelIndex(currentLevel);
        createBodiesIfNeeded();
        if(getLevel().getPlayerReturn() == null)        //Spawn och return är samma
            setPlayerBufferPosition(getLevel().getPlayerSpawn());
        else
            setPlayerBufferPosition(getLevel().getPlayerReturn());
        gameModel.setWorldNeedsUpdate(true);
    }

    public boolean worldNeedsUpdate(){
        return gameModel.worldNeedsUpdate();
    }

    public void setWorldNeedsUpdate(boolean bool){ gameModel.setWorldNeedsUpdate(bool);
    }

    /**
     * @return The player's current (rounded) position as a point
     */
    public Point getPlayerPosition(){
        return new Point(Math.round(gameModel.getPlayer().getX()), Math.round(gameModel.getPlayer().getY()));
    }

    public void updatePlayerPosition(Point point){
        gameModel.getPlayer().setPosition(point);
    }

    public void setPlayerBufferPosition(Point point){
        gameModel.setPlayerBufferPosition(point);
    }


    public Point getPlayerBufferPosition(){
        return gameModel.getPlayerBufferPosition();
    }
}
