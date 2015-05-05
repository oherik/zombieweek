package edu.chalmers.zombie.adapter;

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
    /**
     * Creates a new level based on a tiled map and a Box2D world
     * @param mapPath   The file path to the map
     * @throws NullPointerException if the path name is incorrect or not found
     */
    public Level(String mapPath){
        if(mapPath == null)
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

    public Point coordinatesToTile(Point coordinates){
        int x = Math.round(coordinates.x / tileSize);   //Vill ha som int istället för float.
        int y = Math.round(coordinates.y / tileSize); //Måste flippa, 0,0 är längst ner till vänster i libGDX men högst upp till vänster i Tiled
        return new Point(x,y);
    }

    public Point tileToCoordinates(Point tile){
        int x = Math.round(tile.x * tileSize);
        int y = Math.round(tile.y * tileSize);
        return new Point(x,y);
    }
}
