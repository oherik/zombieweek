package edu.chalmers.zombie.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.controller.ContactListener;

/**
 * The general model for storing a specific level. The level contains a tiled map, which is the graphical representation of the level, and a box2d World which handles the physics.
 */


public class Level {
    private World world;
    private TiledMap tiledMap;

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
}
