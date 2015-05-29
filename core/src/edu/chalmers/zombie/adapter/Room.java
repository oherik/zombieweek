package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.controller.ContactListener;
import edu.chalmers.zombie.utils.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The general model for storing a specific level. The level contains a tiled map, which is the graphical representation
 * of the level and its meta data, and a box2d World which handles the physics. It also stores the zombies for the current
 * level. It furthermore stores a 2d short array which hold the collision data for fast collision lookups in path finding
 * and other algorithms.
 */
public class Room {
    private World world;
    private TiledMap tiledMap;
    private ArrayList zombies, potions;
    private boolean hasBeenTraversed;
    Point playerSpawn, playerReturn;
    TiledMapTileLayer metaLayer;
    TiledMapImageLayer topLayer, bottomLayer;
    private short[][] collisionTileGrid;

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
        metaLayer.setVisible(false);

        //Create the world
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new ContactListener());
        hasBeenTraversed = false;

        zombies = new ArrayList<Zombie>();
        potions = new ArrayList<Potion>();

        collisionTileGrid = new short[metaLayer.getWidth()][metaLayer.getHeight()];

    }

    public void stepWorld(float timeStep, int velocityIterations, int positionIterations){
        world.step(timeStep,velocityIterations,positionIterations);
    }


    public void createBody(ZWBody body, Object userData){
        Body b2body = world.createBody(body.getBodyDef());
        b2body.createFixture(body.getFixtureDef());
        body.setBody(b2body);
        b2body.setUserData(userData);
    }

    public void createFixture(ZWBody body, Object userData){
        Fixture fixture = world.createBody(body.getBodyDef()).createFixture(body.getFixtureDef());
        fixture.setUserData(userData);
    }

    public void destroyBody(ZWBody body){
            this.world.destroyBody(body.getBody());
            body.setBody(null);
            body = null;
    }


    /**
     * Add collision to a tile tile
     * @param x The x coordinate
     * @param y The y coordinate
     * @param bit   The collision bit
     */
    public void addCollision(int x, int y, short bit){
        collisionTileGrid[x][y] += bit;
    }

    /**
     * Remove collision from a tile
     * @param x The x coordinate
     * @param y The y coordinate
     * @param bit   The collision bit
     */
    public void removeCollision(int x, int y, short bit){
        collisionTileGrid[x][y] -= bit;
    }

    /**
     * @return  The collision grid for the zombies as a 2d short array.
     */
    public short[][] getCollisionTileGrid(){
        return collisionTileGrid;
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
     * @return The level's potions
     */
    public ArrayList<Potion> getPotions(){
        return this.potions;
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
     * Adds a potion to the room
     * @param p A potion
     */
    public void addPotion(Potion p) {potions.add(p);}
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
     * Removes a potion
     * @param p The potion to remove
     */
    public void removePotion(Potion p){
        p.dispose();
        potions.remove(p);
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
    public void setHasBeenTraversed(boolean bool) {
        this.hasBeenTraversed = bool;
    }

    /**
     * @return true if the collision objects have been initialized, false if not
     */
    public boolean hasBeenTraversed() {
        return this.hasBeenTraversed;
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

    /**
     * @return The map's width in tiles
     */
    public int getTiledWidth(){
        return tiledMap.getProperties().get("width", Integer.class);
    }

    public boolean hasMetaData(int col, int row){
        TiledMapTileLayer.Cell currentCell = metaLayer.getCell(col, row);
        return (currentCell != null && currentCell.getTile() != null);
    }

    public boolean hasProperty(int col, int row, String property){
        TiledMapTileLayer.Cell currentCell = metaLayer.getCell(col, row);
        return (hasMetaData(col, row) && currentCell.getTile().getProperties().get(property) != null);
    }

    public Object getProperty(int col, int row, String propertyName){
        TiledMapTileLayer.Cell currentCell = metaLayer.getCell(col, row);
        if(hasProperty(col, row,propertyName))
            return currentCell.getTile().getProperties().get(propertyName);
        else
            return null;
    }

    /**
     * @return The map's height in tiles
     */
    public int getTiledHeight(){
        return tiledMap.getProperties().get("height", Integer.class);
    }

}
