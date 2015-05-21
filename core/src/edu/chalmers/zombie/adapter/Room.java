package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.controller.ContactListener;
import edu.chalmers.zombie.utils.Constants;

import java.awt.*;
import java.util.ArrayList;

/**
 * The general model for storing a specific level. The level contains a tiled map, which is the graphical representation
 * of the level and its meta data, and a box2d World which handles the physics. It also stores the zombies for the current
 * level.
 */
public class Room {
    private World world;
    private TiledMap tiledMap;
    private ArrayList zombies;
    private boolean initializedBodies;
    Point playerSpawn, playerReturn;
    TiledMapTileLayer metaLayer;
    TiledMapImageLayer topLayer, bottomLayer;
    boolean[][] zombieNavigationMesh;

    /**
     * Creates a new level based on a tiled map and a Box2D world
     *
     * @param tiledMap The map of the level
     * @throws NullPointerException if the path name is incorrect or not found
     */
    public Room(TiledMap tiledMap) {
        this.tiledMap = tiledMap;

        //Initialize the layers
        metaLayer = (TiledMapTileLayer) tiledMap.getLayers().get(Constants.META_LAYER);
        topLayer = (TiledMapImageLayer) tiledMap.getLayers().get(Constants.TOP_LAYER);
        bottomLayer = (TiledMapImageLayer) tiledMap.getLayers().get(Constants.BOTTOM_LAYER);

        //Create the world
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new ContactListener());
        initializedBodies = false;

        zombies = new ArrayList<Zombie>();

        zombieNavigationMesh = new boolean[metaLayer.getWidth()][metaLayer.getHeight()];
    }

    /**
     * Mark a tile as traversable for the zombies
     * @param x The x coordinate
     * @param y The y coordinate
     * @param traversable   true if traversable, false if not
     */
    public void setZombieNavigationalTile(int x, int y, boolean traversable){
        zombieNavigationMesh[x][y] = traversable;
    }

    /**
     * @return  The traverasble tiles for the zombies
     */
    public boolean[][] getZombieNavigationMesh(){
        return zombieNavigationMesh;
    }

    /**
     * @return The map's meta data layer
     */
    public TiledMapTileLayer getMetaLayer() {
        return this.metaLayer;
    }

    /**
     * @return The map's top graphical layer
     */
    public TiledMapImageLayer getTopLayer() {
        return this.topLayer;
    }

    /**
     * @return The map's bottom graphical layer
     */
    public TiledMapImageLayer getBottomLayer() {
        return this.bottomLayer;
    }

    /**
     * @return The level's zombies
     */
    public ArrayList<Zombie> getZombies() {
        return zombies;
    }

    /**
     * Adds a zombie
     *
     * @param z A zombie
     */
    public void addZombie(Zombie z) {
        zombies.add(z);
    }

    /**
     * Removes a zombie
     *
     * @param z A zombie to remove
     */
    public void removeZombie(Zombie z) {
        z.dispose();
        zombies.remove(z);
    }

    /**
     * @return the level's map
     */
    public TiledMap getMap() {
        return this.tiledMap;
    }

    /**
     * @return the level's Box2D World
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Destroys an entity body
     *
     * @param e The entity which body will be destroyed
     */
    public void destroyBody(Entity e) {
        e.removeBody();
    }

    /**
     * Destroys a body
     *
     * @param b The body which will be destroyed
     */
    public void destroyBody(Body b) {
        getWorld().destroyBody(b);
        b = null; //To avoid null pointer exceptions
    }

    /**
     * Sets a variable which is true if the bodies (collision objects) have been initialized
     *
     * @param bool true if the collision objects have been initialized, false if not
     */
    public void setInitializedBodies(boolean bool) {
        this.initializedBodies = bool;
    }

    /**
     * @return true if the collision objects have been initialized, false if not
     */
    public boolean hasInitializedBodies() {
        return this.initializedBodies;
    }

    /**
     * @return The spawn point for the player
     */
    public Point getPlayerSpawn() {
        return this.playerSpawn;
    }

    /**
     * @return The point where the player shall be placed if she returns to this level
     */
    public Point getPlayerReturn() {
        return this.playerReturn;
    }

    /**
     * Sets where the player will spawn
     *
     * @param playerSpawn A point on the map where the player will spawn
     */
    public void setPlayerSpawn(Point playerSpawn) {
        this.playerSpawn = playerSpawn;
    }

    /**
     * Sets where the player will be placed if she returns to this level
     *
     * @param playerReturn A point on the map where the player will be placed if she returns to this level
     */
    public void setPlayerReturn(Point playerReturn) {
        this.playerReturn = playerReturn;
    }
}
