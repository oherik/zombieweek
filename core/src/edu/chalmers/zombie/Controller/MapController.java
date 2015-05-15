package edu.chalmers.zombie.controller;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
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
 * This controller class makes all the different calculations regarding the maps, levels, worlds and objects in them.
 */
public class MapController {
    GameModel gameModel;

    /**
     * Constructor
     */
    public MapController(){
        this.gameModel = GameModel.getInstance();
    }

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

    public void initializeLevels(){ //TODO varifrån ska vi hämta dessa?

        gameModel.addLevel(new Level("core/assets/Map/Test_world_2_previous.tmx"));
        gameModel.addLevel(new Level("core/assets/Map/Test_world_3.tmx"));
        gameModel.addLevel(new Level("core/assets/Map/Test_world_2_next.tmx"));

    }

    /**
     * Creates the different collision objects (see CollisionObject.java) that represent the physical world and stores
     * them in the model.
     */
    public void initializeCollisionObjects(){
        //Create and define body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; //The collision objects shouldn't move

        //Define shapes
        PolygonShape standardBoxShape = new PolygonShape();
        standardBoxShape.setAsBox(0.5f, 0.5f);   //The size is set as 2 * the values inside the parantheses
        PolygonShape doorShape = new PolygonShape();    //The door is thinner, so the player doesn't accidentally bump into them
        doorShape.setAsBox(0.25f, 0.5f); //The size is set as 2 * the values inside the parantheses

        //Create a new ArrayList to store the objects
        ArrayList<CollisionObject> collisionObjects = new ArrayList<CollisionObject>();

        //Water
        FixtureDef fixDef = new FixtureDef();
        fixDef.friction = 0;
        fixDef.restitution = .1f;
        fixDef.shape = standardBoxShape;
        fixDef.filter.categoryBits = Constants.COLLISION_WATER;
        fixDef.filter.maskBits = Constants.COLLISION_ZOMBIE;
        collisionObjects.add(new CollisionObject("water", bodyDef, fixDef));

        //Collision for all
        fixDef = new FixtureDef();  //Reset the fixture definition, this has to be done for each new object
        fixDef.friction = 0.2f;
        fixDef.restitution = .1f;
        fixDef.shape = standardBoxShape;
        fixDef.filter.categoryBits = Constants.COLLISION_OBSTACLE;
        fixDef.filter.maskBits = Constants.COLLISION_ENTITY | Constants.COLLISION_PROJECTILE;
        collisionObjects.add(new CollisionObject(Constants.COLLISION_PROPERTY_ALL, bodyDef, fixDef));

        //Door
        fixDef = new FixtureDef();;
        fixDef.shape = doorShape;
        fixDef.filter.categoryBits = Constants.COLLISION_DOOR;
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
     * This function creates all the Box2d obstacle bodies. The obstacles are the CollisionObjects defined in
     * initializeCollisionObjects() and stored in GameModel.  An obstacle might be a wall, a river or anything else that
     * the player, zombie or projectile should be able to collide with one way or another. The boxes are created by the
     * definitions stored in the collision object array list.
     *
     *
     * It goes through all the tiles in the map looking for tiles containing any of the collision names in the
     * collision object array list. If one is found a box2d fixture is placed there, which allows for collision detection.
     * The fixture then has its user data set to a refernce to the object in question, so it can be called upon
     * during the collision detection.
     *
     * If a tile is found where a door should be placed the door property is stored as a property in the door collision
     * object. The door property is the level which should be loaded if the player touches the door. A new door collision
     * object is then added to the collision object array list, in case any more door is found (the same can't be re-used
     * since the property of the doors in most cases are unique). The door is then removed from the array list.
     *
     * @param collisionObjects the list of all the collision objects that can be placed in the world
     */

    private void createBodiesIfNeeded(ArrayList<CollisionObject> collisionObjects) {
        Level level = getLevel();
        if(!level.hasInitializedBodies()) { //if the level already has these initialized there's no point in continuing
            World world = getWorld();
            TiledMapTileLayer metaLayer = getMapMetaLayer();

            String zombieSpawn = "zombie_spawn"; //TODO test tills vi får flera sorters zombies
            String playerSpawn = "player_spawn"; //TODO test tills ovan är fixat
            String playerReturn = "player_return"; //TODO test tills ovan är fixat

            if (metaLayer != null) {
                metaLayer.setVisible(false);
                for (int row = 0; row < metaLayer.getHeight(); row++) {       //TODO onödigt att gå igenom allt?
                    for (int col = 0; col < metaLayer.getWidth(); col++) {
                        TiledMapTileLayer.Cell currentCell = metaLayer.getCell(col, row);
                        if (currentCell != null && currentCell.getTile() != null) {        //There's a meta data tile on that position
                            CollisionObject toAdd = null;
                            CollisionObject toRemove = null;
                            for (CollisionObject obj : collisionObjects) {
                                if (currentCell.getTile().getProperties().get(obj.getName()) != null) {
                                    obj.getBodyDef().position.set((col + 0.5f), (row + 0.5f));
                                    if(obj.getName().equals(Constants.DOOR_PROPERTY)){
                                        toAdd = obj.clone();
                                        toRemove = obj;
                                        obj.setProperty((String) currentCell.getTile().getProperties().get(Constants.DOOR_PROPERTY));
                                    }
                                    Fixture fixture = world.createBody(obj.getBodyDef()).createFixture(obj.getFixtureDef());
                                    fixture.setUserData(obj);
                                }
                            }
                            if(toRemove != null) {
                                collisionObjects.remove(toRemove);
                            }
                            if(toAdd != null) {
                                collisionObjects.add(toAdd);
                            }
                            if (currentCell.getTile().getProperties().get(zombieSpawn) != null) {           //TODO skapa en spawnEntities-metod istället. Och en huvudmetod som går igenom båda metoderna
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

    /**
     * Runs createBodiesIfNeeded using the default values stored in the game model.
     */
    public void createBodiesIfNeeded() {
       createBodiesIfNeeded(gameModel.getCollisionObjects());
    }

    /**
     * @param levelIndex the level index that will be accessed
     * @return The level specified by the index
     * @throws  IndexOutOfBoundsException if the user tries to access a level not in range
     */
    public Level getLevel(int levelIndex){
        int maxSize = gameModel.getLevels().size() -1;
        if(levelIndex<0 ||levelIndex > maxSize)
            throw new IndexOutOfBoundsException("Not a valid level index, must be between " + 0 + " and  " + maxSize);
        return gameModel.getLevel(levelIndex);
    }

    /**
     * @param levelIndex the level index which bottom layer will be accessed
     * @return The bottom layer specified by the index
     */
    public TiledMapImageLayer  getMapBottomLayer(int levelIndex){
        return getLevel(levelIndex).getBottomLayer();
    }

    /**
     * @param levelIndex the level index which top layer will be accessed
     * @return The top layer specified by the index
     */
    public TiledMapImageLayer  getMapTopLayer(int levelIndex){
        return getLevel(levelIndex).getTopLayer();
    }

    /**
     * @param levelIndex the level index which meta layer will be accessed
     * @return The meta layer specified by the index
     */
    public TiledMapTileLayer getMapMetaLayer(int levelIndex){
        return getLevel(levelIndex).getMetaLayer();
    }

    /**
     * @return The current bottom layer
     */
    public TiledMapImageLayer getMapBottomLayer(){
        return getMapBottomLayer(gameModel.getCurrentLevelIndex());
    }

    /**
     * @return The current top layer
     */
    public TiledMapImageLayer getMapTopLayer(){
        return getMapTopLayer(gameModel.getCurrentLevelIndex());
    }

    /**
     * @return The current meta layer
     */
    public TiledMapTileLayer getMapMetaLayer(){
        return getMapMetaLayer(gameModel.getCurrentLevelIndex());
    }

    /**
     * Loads the new level in the game model, creates bodies if needed and sets that the renderer needs to update the
     * world. It also updates where the player will be placed after the world step function (it's not possible to do it
     * at the same time, thus a temporary point is stored in the model). This is decided based on if the level that's
     * being loaded is before or after the one just shown to the user. It then creates the collision bodies for the new
     * level, if needed and sets a variable in the game model that the renderer needs to update the world in the next
     * world step.
     *
     * @param levelIndex the level to load
     * @throws  IndexOutOfBoundsException if the user tries to access a level not in range
     */
    public void loadLevel(int levelIndex){
        int maxSize = gameModel.getLevels().size() -1;
        if(levelIndex<0 ||levelIndex > maxSize)
            throw new IndexOutOfBoundsException("Not a valid level index, must be between " + 0 + " and  " + maxSize);
        int oldLevelIndex = gameModel.getCurrentLevelIndex();
        gameModel.setCurrentLevelIndex(levelIndex);
        if(oldLevelIndex>levelIndex){
            if(getLevel().getPlayerReturn() == null)        //If the spawn and return points are the same point in the map file
                setPlayerBufferPosition(getLevel().getPlayerSpawn());
            else
                setPlayerBufferPosition(getLevel().getPlayerReturn());
        }
        else
            setPlayerBufferPosition(getLevel().getPlayerSpawn());
        createBodiesIfNeeded();
        gameModel.setWorldNeedsUpdate(true);
    }

    /**
     * @return true if the world needs to be updated, false if not
     */
    public boolean worldNeedsUpdate(){
        return gameModel.worldNeedsUpdate();
    }

    /**
     * If the world needs to update the next step, this variable is set in the model
     * @param bool true if the world needs to be updated, false if not
     */
    public void setWorldNeedsUpdate(boolean bool){
        gameModel.setWorldNeedsUpdate(bool);
    }

    /**
     * @return The player's current (rounded) position as a point
     */
    public Point getPlayerPosition(){
        return new Point(Math.round(gameModel.getPlayer().getX()), Math.round(gameModel.getPlayer().getY()));
    }

    /**
     * Updates the player's position
     * @param point Where the player will be placed
     */
    public void updatePlayerPosition(Point point){
        gameModel.getPlayer().setPosition(point);
    }

    /**
     * Sets where the player should be when the world step is done
     * @param point Where the player will be placed after the step
     */
    public void setPlayerBufferPosition(Point point){
        gameModel.setPlayerBufferPosition(point);
    }

    /**
     * @return where the player will be placed after the step
     */
    public Point getPlayerBufferPosition(){
        return gameModel.getPlayerBufferPosition();
    }

    /**
     * Checks if the path is obstructed by a wall using a modified version of Bresenham's line algorithm while taking into account how the maps are constructed.
     * @param position  The original position
     * @param metaLayer The map's meta layer
     * @param distance  The distance in which to check
     * @param angle the angle to check, in radians (0 is east, pi is west)
     * @return  true if the path is obstructed, false otherwise
     */
    public static boolean pathObstructed(Vector2 position, TiledMapTileLayer metaLayer, float distance, float angle){
    /* Input checks */
        if(position == null ||metaLayer == null)
            throw new NullPointerException("The input mustn't be null");
        if(position.x < 0 || position.y < 0)
            throw new IndexOutOfBoundsException("The position must be positive");   //TODO kanske inte behövs i och med att det checkas i relevant metod
        if(position.x > metaLayer.getWidth() || position.y > metaLayer.getHeight())
            throw new IndexOutOfBoundsException("The position must be within meta layer bounds");//TODO kanske inte behövs i och med att det checkas i relevant metod
        if(distance < 0)
            throw new IndexOutOfBoundsException("The distance must be positive");
        Vector2 endPosition = position.add(distance * (float) Math.cos(angle), distance * (float) Math.sin(angle));
        /* Set the west-most point as origin. This is necessary for the rest of the algorithm to work */ //TODO kanske kan gå runt det?
        if(position.x>endPosition.x){
            Vector2 temp = endPosition;
            endPosition = position;
            position=temp;
        }
        /* Extract and convert the positions to map coordinates */
        int x_origin = Math.round(position.x - position.x % 1 - 0.5f);
        int y_origin = Math.round(position.y - position.y % 1 - 0.5f);
        int x_end = Math.round(endPosition.x - endPosition.x % 1 - 0.5f);
        int y_end = Math.round(endPosition.y - endPosition.y % 1 - 0.5f);

        int dx = x_end - x_origin;
        int dy = y_end - y_origin;
        double error = 0;
        double deltaError = ((double)dy/(double)dx < 0) ? -(double)dy/(double)dx : (double)dy/(double)dx;
        int ySign = (y_end-y_origin)<0 ? -1 : 1;
        int y = y_origin;
        for(int x = x_origin; x <= x_end && x>=0 && x<metaLayer.getWidth(); x++){
            if(wallAt(x,y,metaLayer)) {
                return true;
            }
            error = error + deltaError;
            while(error>=0.5 && y>= 0 && y < metaLayer.getHeight()){
                if(wallAt(x,y,metaLayer)) {
                    return true;
                }
                y = y + ySign;
                error = error -1;
            }
        }
        return false;
    }

    /**
     * Checks if there's a wall tile at the given position
     * @param position  the position to check at
     * @param metaLayer the map's meta layer
     * @return true if there's a wall there, false otherwise
     * @throws NullPointerException if the position or meta layer are empty
     */
    public static boolean wallAt(Point position, TiledMapTileLayer metaLayer){
        if(position == null  ||metaLayer == null)
            throw new NullPointerException("The input mustn't be null");

        int x = Math.round(position.x - position.x%1);
        int y = Math.round(position.y - position.y%1);

        return wallAt(x,y,metaLayer);
    }

    /**
     * Checks if there is a collision_all tile at a given position
     * @param x The x coordinate
     * @param y The  coordinate
     * @param metaLayer The map's meta layer
     * @return  true if there's a collision tile at that position, false otherwise
     */
    public static boolean wallAt(int x, int y, TiledMapTileLayer metaLayer){
        if(metaLayer == null)
            throw new NullPointerException("The meta layer mustn't be null");
        if(x<0 ||y < 0 || x > metaLayer.getWidth()-1 ||y > metaLayer.getHeight()-1 )
            throw new IndexOutOfBoundsException("The input coordinates must be withing the meta layer bounds");
        return (metaLayer.getCell(x,y) != null && metaLayer.getCell(x,y) != null && metaLayer.getCell(x,y).getTile().getProperties().get(Constants.COLLISION_PROPERTY_ALL) != null);
    }
}
