package edu.chalmers.zombie.model;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.controller.controller_adapters.ZWContactListener;
import edu.chalmers.zombie.utils.Constants;
import java.awt.*;
import java.util.ArrayList;

/**
 * The general model for storing a specific level. The level contains a tiled map, which is the graphical representation
 * of the level and its meta data, and a box2d World which handles the physics. It also stores the zombies for the current
 * level. It furthermore stores a 2d short array which hold the collision data for fast collision lookups in path finding
 * and other algorithms.
 */
public class Room {
    private ZWWorld world;
    private ZWTiledMap tiledMap;
    private ArrayList zombies, potions, books;
    private boolean hasBeenTraversed;
    Point playerSpawn, playerReturn;
    private short[][] collisionTileGrid;

    /**
     * Creates a new level based on a tiled map and a Box2D world
     *
     * @param tiledMap The map of the level
     * @throws NullPointerException if the path name is incorrect or not found
     */
    public Room(ZWTiledMap tiledMap) {
        this.tiledMap = tiledMap;

        //Initialize the layers
        //metaLayer = (TiledMapTileLayer) tiledMap.getLayers().get(Constants.META_LAYER);
        //topLayer = (TiledMapImageLayer) tiledMap.getLayers().get(Constants.TOP_LAYER);
        //bottomLayer = (TiledMapImageLayer) tiledMap.getLayers().get(Constants.BOTTOM_LAYER);
        tiledMap.setVisibible(Constants.META_LAYER, false);

        //Create the world
        world = new ZWWorld();
        world.setContactListener(new ZWContactListener());
        hasBeenTraversed = false;

        zombies = new ArrayList<Zombie>();
        potions = new ArrayList<Potion>();
        books = new ArrayList<Book>();

        collisionTileGrid = new short[tiledMap.getTiledWidth()][tiledMap.getTiledHeight()];

    }

    public void stepWorld(float timeStep, int velocityIterations, int positionIterations){
        world.step(timeStep,velocityIterations,positionIterations);
    }


    public void createBody(ZWBody body, Object userData){
        ZWBody newBody = world.createBody(body);
        newBody.setUserData(userData);
    }

    public void createFixture(ZWBody body, Object userData){
        ZWFixture fixture = world.createBody(body).createFixture(body.getFixtureDef());
        fixture.setUserData(userData);
    }

    public void destroyBody(ZWBody body){
            this.world.destroyBody(body);
           // body.setBody(null);
            //body = null;
    }

    public void addBook(Book b){
        books.add(b);
    }

    public void removeBook(Book b){
        books.remove(b);
    }

    public ArrayList<Book> getBooks(){
        return books;
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
        potions.remove(p);
    }

    /**
     * @return the level's map
     */
    public ZWTiledMap getMap() {
        return this.tiledMap;
    }

    /**
     * @return the level's Box2D World
     */
    public ZWWorld getWorld() {
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
        return tiledMap.getTiledWidth();
    }

    public boolean hasMetaData(int col, int row){
        return !tiledMap.tileIsEmpty(Constants.META_LAYER, col, row);
    }

    public boolean hasProperty(int col, int row, String property){
        return tiledMap.hasProperty(Constants.META_LAYER, col, row, property);
    }

    public Object getProperty(int col, int row, String propertyName){
       return tiledMap.getProperty(Constants.META_LAYER, col, row, propertyName);
    }

    /**
     * @return The map's height in tiles
     */
    public int getTiledHeight(){
        return tiledMap.getTiledHeight();
    }

}
