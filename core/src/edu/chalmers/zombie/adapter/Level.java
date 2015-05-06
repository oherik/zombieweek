package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.controller.ContactListener;
import edu.chalmers.zombie.utils.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    /**
     * Creates a new level based on a tiled map and a Box2D world
     * @param mapPath  The file path to the map containing the meta data
     * @param mapPaintingPath  The file path to the map painting
     * @throws NullPointerException if the path name is incorrect or not found
     */
    public Level(String mapPath, String mapPaintingPath){
        if(mapPath == null || mapPaintingPath == null)
            throw new NullPointerException("Level: no path name recieved");
        tiledMap = new TmxMapLoader().load(mapPath);
        if(tiledMap == null)
            throw new NullPointerException("Level: incorrect path name");
        world = new World(new Vector2(0,0), true);
        world.setContactListener(new ContactListener());
        tileSize = (float) Constants.TILE_SIZE;
        zombies = new ArrayList<Zombie>();
        BodyDef bd =new BodyDef();
        ground = world.createBody(bd);  //Behövs för friktion
        mapPainting = new Sprite(new Texture(mapPaintingPath));
    }

    /**
     * If a top layer is included
     * @param mapPath  The file path to the map containing the meta data
     * @param mapPaintingPath  The file path to the map painting
     * @param mapPaintingTopLayerPath  The file path to the map painting's top layer
     */
    public Level(String mapPath, String mapPaintingPath, String mapPaintingTopLayerPath){
        this(mapPath,mapPaintingPath);
        if(mapPaintingTopLayerPath == null)
            throw new NullPointerException("Level: no top layer path name recieved");
        mapPaintingTopLayer = new Sprite(new Texture(mapPaintingTopLayerPath));
    }

    public Body getGround(){
        return  ground;
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
}
