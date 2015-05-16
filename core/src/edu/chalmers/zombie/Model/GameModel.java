package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.Texture;       //TODO debug
import com.badlogic.gdx.graphics.g2d.Sprite;    //TODO debug
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.testing.ZombieTest;
import edu.chalmers.zombie.utils.Direction;
import edu.chalmers.zombie.utils.GameState;
import edu.chalmers.zombie.utils.ResourceManager;

import java.awt.*;
import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/** Stores the game data. The model implements the singleton pattern
 * Created by Tobias on 15-04-02.
 */
public class GameModel {

    private static GameModel instance = new GameModel();
    private Player player;
    private Zombie zombie;
    private ArrayList<Level> levels;
    private int currentLevel;
    private ArrayList<Book> books = new ArrayList<Book>();
    private Set entitiesToRemove;
    private ArrayList<CollisionObject> collisionObjects;
    private String metaLayerName;
    private  boolean worldNeedsUpdate; //If a map change has been called
    private Point playerBufferPosition; //Can't alter the player position directly in the world step
    public static ResourceManager res;
    private boolean stepping = false;
    private GameState gameState; //the state of the game


    /**
     * Initializes the game model
     */
    private GameModel(){
        metaLayerName = "meta";
        currentLevel = 1;   //TODO test
        res = new ResourceManager();
        res.loadTexture("player","core/assets/player_professional_final_version.png");
        levels = new ArrayList<Level>();
        entitiesToRemove = new HashSet<Entity>();
        worldNeedsUpdate = true;
        //addTestLevel();                                 //TODO debug
        //addTestLevel_2();                                 //TODO debug
        //addTestPlayer();                                //TODO debug
       // addTestZombie();                                //TODO debug
    }

    /**
     * Only for debug
     *//*
    private void addTestLevel(){
        levels.add(new Level("core/assets/Map/Test_v2.tmx", "core/assets/Map/testmap.png", "core/assets/Map/testmap_top.png"));
    }
*/
    /**
     * Only for debug
     */
    /*
    private void addTestLevel_2(){
        levels.add(new Level("core/assets/Map/Test_world_2.tmx", "core/assets/Map/Test_world_2_bottom.png", "core/assets/Map/Test_world_2_top.png"));
    }
    */
    /**
     * Only for debug
     */
    private void addTestPlayer(){
        player = new Player(new Sprite(res.getTexture("player")),levels.get(0).getWorld(),0,0);

    }

    /**
     * Only for debug
     */
    private void addTestZombie(){
        zombie = new ZombieTest(levels.get(0).getWorld(),2,2);
        getLevel().addZombie(zombie);
    }



    /**
     * @return  The current instance of the game model
     */
    public static GameModel getInstance( ) {
        return instance;
    }

    /**
     * @return  The player
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Sets the player
     * @param player The player
     */
    public void setPlayer(Player player){
        this.player = player;
    }

    public Zombie getZombie(){
        return zombie;
    }
    /**
     * Adds a level
     */
    public void addLevel(Level level){
        levels.add(level);
    }

    /**
     * Sets all levels
     */
    private void setLevels(ArrayList<Level> levels){
        this.levels = levels;
    }



    /**
     * @return  The current level
     */
    public Level getLevel(){return levels.get(currentLevel); }

    /**
     * @return  The level specified by an index
     * @throws  IndexOutOfBoundsException if the index is non-valid
     */
    public Level getLevel(int levelIndex){
        if(levelIndex >= levels.size())
            throw new IndexOutOfBoundsException("GameModel: the getLevel index exceeds array size");
        currentLevel = levelIndex;
        return levels.get(levelIndex);
    }

    /**
     * @return  The index for the current level
     */
    public int getCurrentLevelIndex(){
        return this.currentLevel;
    }

    /**
     * @return  Sets the current level
     * @throws  IndexOutOfBoundsException if the index is < 0
     */
    public void setCurrentLevelIndex(int i){
        if(i < 0)
            throw new IndexOutOfBoundsException("GameModel: current level must be >= 0");
        this.currentLevel = i;
    }

    /**
     * Updates the player position
     */
    public void movePlayer(Direction direction){
        player.move(direction);
    }

    public ArrayList<Book> getBooks(){
        return books;
    }

    public void addBook(Book book){
        books.add(book);
    }

    public void addEntitiesToRemove(Set<Entity> entitySet){this.entitiesToRemove = entitySet; }

    public void addEntityToRemove(Entity entity){this.entitiesToRemove.add(entity); }

    public void clearEntitiesToRemove(){this.entitiesToRemove.clear();}

    public Set<Entity> getEntitiesToRemove() {return this.entitiesToRemove; }

    /**
     * @return The current level's zombies
     */
    public ArrayList<Zombie> getZombies(){
        return getLevel().getZombies();
    }

    public ArrayList<CollisionObject> getCollisionObjects(){
        return this.collisionObjects;
    }

    public void setCollisionObjects(ArrayList<CollisionObject> collisionObjects){
        this.collisionObjects=collisionObjects;
    }

    public void addCollisionObjects(CollisionObject obj){
        this.collisionObjects.add(obj);
    }

    public String getMetaLayerName(){
        return this.metaLayerName;
    }

    public ArrayList<Level> getLevels(){
        return levels;
    }

    public void setWorldNeedsUpdate(boolean bool){
        this.worldNeedsUpdate = bool;
    }

    public boolean worldNeedsUpdate(){
        return this.worldNeedsUpdate;
    }

    public void setPlayerBufferPosition(Point point){
        this.playerBufferPosition = point;
    }

    public Point getPlayerBufferPosition(){
        return this.playerBufferPosition;
    }
    //These two methods are keeping track of world.step().
    public boolean isStepping(){
        return stepping;
    }
    public void setStepping(boolean s){
        this.stepping = s;
    }

    public GameState getGameState(){return gameState;}

    public void setGameState(GameState gameState){this.gameState = gameState;}
}
