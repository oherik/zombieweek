package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.controller.ContactListener;
import edu.chalmers.zombie.utils.Constants;

import java.awt.*;
import java.util.ArrayList;

/**
 * The general model for storing a specific level. The level contains a tiled map, which is the graphical representation of the level, and a box2d World which handles the physics.
 */


public class Level {
    private World world;
    private TiledMap tiledMap;
    private float tileSize;
    private ArrayList zombies;
    private Body ground;
    private Sprite mapPainting, mapPaintingTopLayer;
    private boolean initializedBodies;
    Point playerSpawn, playerReturn;
    TiledMapTileLayer metaLayer;
    TiledMapImageLayer topLayer, bottomLayer;
    /**
     * Creates a new level based on a tiled map and a Box2D world
     * @param mapPath  The file path to the map containing the meta data
     * @throws NullPointerException if the path name is incorrect or not found
     */
    public Level(String mapPath){
        if(mapPath == null)
            throw new NullPointerException("Level: no path name recieved");
        tiledMap = new TmxMapLoader().load(mapPath);
        if(tiledMap == null)
            throw new NullPointerException("Level: incorrect path name");

        //Initialize the layers
        metaLayer = (TiledMapTileLayer) tiledMap.getLayers().get(Constants.META_LAYER);
        topLayer = (TiledMapImageLayer) tiledMap.getLayers().get(Constants.TOP_LAYER);
        bottomLayer = (TiledMapImageLayer) tiledMap.getLayers().get(Constants.BOTTOM_LAYER);
        tileSize = (float) Constants.TILE_SIZE;

        //Create the world
        world = new World(new Vector2(0,0), true);
        world.setContactListener(new ContactListener());
        initializedBodies = false;


        zombies = new ArrayList<Zombie>();
    }

    public TiledMapTileLayer getMetaLayer(){
        return this.metaLayer;
    }

    public TiledMapImageLayer getTopLayer(){
        return this.topLayer;
    }

    public TiledMapImageLayer getBottomLayer(){
        return this.bottomLayer;
    }

    /**
     * @return The level's zombies
     */
    public ArrayList<Zombie> getZombies(){
        return zombies;
    }

    /**
     * Adds a zombie
     * @param z A zombie
     */
    public void addZombie(Zombie z){
        zombies.add(z);
    }

    /**
     * Removes a zombie
     * @param z A zombie to remove
     */
    public void removeZombie(Zombie z){
        z.dispose();
        zombies.remove(z);
    }


    /**
     * @return the level's map
     */
    public TiledMap getMap(){
        return this.tiledMap;
    }

    /**
     * @return the level's Box2D World
     */
    public World getWorld(){
        return this.world;
    }

    /**
     * Destroys an entity body
     * @param e The entity which body will be destroyed
     */
    public void destroyBody(Entity e){
        e.removeBody();
    }

    /**
     * Destroys a body
     * @param b The body which will be destroyed
     */
    public void destroyBody(Body b){
        getWorld().destroyBody(b);
        b=null;
    }

    /**
     * @return The map painting
     */
    public Sprite getMapPainting(){
        return mapPainting;
    }
    /**
     * @return The map painting to be rendered on top of the player and other entities
     */
    public Sprite getMapPaintingTopLayer(){
        return mapPaintingTopLayer;
    }

    public void setInitializedBodies(boolean bool){
        this.initializedBodies = bool;
    }

    public boolean hasInitializedBodies(){
        return this.initializedBodies;
    }

    public Point getPlayerSpawn(){
        return this.playerSpawn;
    }

    public Point getPlayerReturn(){
        return this.playerReturn;
    }

    public void setPlayerSpawn(Point playerSpawn){
        this.playerSpawn = playerSpawn;
    }

    public void setPlayerReturn(Point playerReturn){
        this.playerReturn = playerReturn;
    }

}
