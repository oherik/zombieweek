package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.Texture;       //TODO debug
import com.badlogic.gdx.graphics.g2d.Sprite;    //TODO debug
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.testing.ZombieTest;
import edu.chalmers.zombie.utils.Direction;

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

    /**
     * Initializes the game model
     */
    private GameModel(){
        currentLevel = 0;
        levels = new ArrayList<Level>();
        entitiesToRemove = new HashSet<Entity>();
        addTestLevel();                                 //TODO debug
        addTestPlayer();                                //TODO debug
        addTestZombie();                                //TODO debug
    }

    /**
     * Only for debug
     */
    private void addTestLevel(){
        levels.add(new Level("core/assets/Map/Test_v2.tmx", "core/assets/Map/testmap.png", "core/assets/Map/testmap_top.png"));
    }

    /**
     * Only for debug
     */
    private void addTestPlayer(){
        player = new Player(new Sprite(new Texture("core/assets/player_professional_final_version.png")),levels.get(0).getWorld(),0,0);
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

    public Zombie getZombie(){
        return zombie;
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
     * @return  The next levél in order
     * @throws  IndexOutOfBoundsException if the current level is the last
     */
    public Level getNextLevel(){
        if(this.currentLevel ==this.levels.size()-1)
            throw new IndexOutOfBoundsException("GameModel: already at last indexed level");
        currentLevel+=1;
        return this.levels.get(currentLevel);
    }

    /**
     * @return  The previous level in order
     * @throws  IndexOutOfBoundsException if the current map is the first
     */
    public Level getPreviousMap(){
        if(this.currentLevel == 0)
            throw new IndexOutOfBoundsException("GameModel: already at first indexed level");
        currentLevel-=1;
        return this.levels.get(currentLevel);
    }

    /**
     * @return  The index for the current level
     */
    public int getCurrentLevelIndex(){
        return this.currentLevel;
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

}
