package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;    //TODO debug
import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.testing.ZombieTest;
import edu.chalmers.zombie.utils.Direction;
import edu.chalmers.zombie.utils.GameState;
import edu.chalmers.zombie.utils.ResourceManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/** Stores the game data. The model implements the singleton pattern
 * Created by Tobias on 15-04-02.
 */
public class GameModel {

    private static GameModel instance = new GameModel();
    private Player player;
    private Zombie zombie;
    private ArrayList<Room> rooms;
    private ArrayList<Level> levels;
    private int currentLevel;
    private int currentRoom;
    private ArrayList<Book> books = new ArrayList<Book>();
    private Set entitiesToRemove;
    private ArrayList<CollisionObject> collisionObjects;
    private String metaLayerName;
    private  boolean worldNeedsUpdate; //If a map change has been called
    private Point playerBufferPosition; //Can't alter the player position directly in the world step
    public static ResourceManager res;
    private AtomicBoolean stepping;
    private GameState gameState; //the state of the game
    private int highestCompletedLevel;
    private boolean flashlightEnabled = false;
    private int highestCompletedRoom;
    private boolean soundOn;
    private Renderer renderer;

    /**
     * Initializes the game model
     */
    private GameModel(){
        metaLayerName = "meta";
        currentRoom = 1;   //TODO test
        res = new ResourceManager();
        res.loadTexture("player","core/assets/player_professional_final_version.png");
        res.loadTexture("emilia","core/assets/Images/emilia.png");   //Set still image frame, TODO: should get still frame from constructor
        res.loadTexture("zombie-still","core/assets/Images/zombie-still.png");
        res.loadTexture("zombie-dead","core/assets/Images/zombie-dead.png");
        res.loadTexture("zombie", "core/assets/Images/zombie.png");
        res.loadTexture("zombie-data-still","core/assets/Images/zombie-data-still.png");
        res.loadTexture("zombie-data-dead","core/assets/Images/zombie-data-dead.png");
        res.loadTexture("zombie-data", "core/assets/Images/zombie-data.png");
        res.loadTexture("zombie-it-still","core/assets/Images/zombie-it-still.png");
        res.loadTexture("zombie-it-dead","core/assets/Images/zombie-it-dead.png");
        res.loadTexture("zombie-it", "core/assets/Images/zombie-it.png");




        res.loadSound("throw", "core/assets/Audio/Sound_effects/throw_book.mp3");
        res.loadSound("menu_hover", "core/assets/Audio/Sound_effects/menu_hover.mp3");
        res.loadSound("zombie_hit","core/assets/Audio/Sound_effects/zombie_hit.mp3");
        res.loadSound("pick_up_book","core/assets/Audio/Sound_effects/pick_up_book.mp3");
        res.loadSound("zombie_sleeping","core/assets/Audio/Sound_effects/zombie_sleeping.mp3");


        renderer = new Renderer();
        stepping=new AtomicBoolean(false);

        rooms = new ArrayList<Room>();
        entitiesToRemove = new HashSet<Entity>();
        worldNeedsUpdate = true;
        //addTestLevel();                                 //TODO debug
        //addTestLevel_2();                                 //TODO debug
        //addTestPlayer();                                //TODO debug
       // addTestZombie();                                //TODO debug

        soundOn = true;
       }


    /**
     * @return The game's renderer
     */
    public Renderer getRenderer(){
        return this.renderer;
    }
    /**
     * Only for debug
     *//*
    private void addTestLevel(){
        rooms.add(new Room("core/assets/Map/Test_v2.tmx", "core/assets/Map/testmap.png", "core/assets/Map/testmap_top.png"));
    }
*/
    /**
     * Only for debug
     */
    /*
    private void addTestLevel_2(){
        rooms.add(new Room("core/assets/Map/Test_world_2.tmx", "core/assets/Map/Test_world_2_bottom.png", "core/assets/Map/Test_world_2_top.png"));
    }
    */
    /**
     * Only for debug
     */
    private void addTestPlayer(){
        player = new Player(res.getTexture("player"), rooms.get(0).getWorld(),0,0);

    }

    /**
     * Only for debug
     */
    private void addTestZombie(){
        zombie = new ZombieTest(rooms.get(0).getWorld(),2,2);
        getRoom().addZombie(zombie);
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
     * Adds a room
     */
    public void addRoom(Room room){
        rooms.add(room);
    }

    /**
     * Sets all rooms
     */
    private void setRooms(ArrayList<Room> rooms){
        this.rooms = rooms;
    }



    /**
     * @return  The current room
     */
    public Room getRoom(){return rooms.get(currentRoom); }

    /**
     * @return  The room specified by an index
     * @throws  IndexOutOfBoundsException if the index is non-valid
     */
    public Room getRoom(int roomIndex) throws IndexOutOfBoundsException{
        if(roomIndex >= rooms.size())
            throw new IndexOutOfBoundsException("GameModel: the getRoom index exceeds array size");
        currentRoom = roomIndex;
        return rooms.get(roomIndex);
    }

    /**
     * @return  The index for the current room
     */
    public int getCurrentRoomIndex(){
        return this.currentRoom;
    }

    /**
     * @return  Sets the current room
     * @throws  IndexOutOfBoundsException if the index is < 0
     */
    public void setCurrentRoomIndex(int i) throws IndexOutOfBoundsException{
        if(i < 0)
            throw new IndexOutOfBoundsException("GameModel: current room must be >= 0");
        this.currentRoom = i;
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
     * @return The current room's zombies
     */
    public ArrayList<Zombie> getZombies(){
        return getRoom().getZombies();
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

    public ArrayList<Room> getRooms(){
        return rooms;
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
        return stepping.get();
    }
    public void setStepping(boolean s){
        this.stepping.set(s);
    }

    public GameState getGameState(){return gameState;}

    public void setGameState(GameState gameState){this.gameState = gameState;}

    public void setHighestCompletedRoom(int room){this.highestCompletedRoom = room;}

    public int getHighestCompletedRoom(){return highestCompletedRoom;}

    public void clearBookList(){
        this.books.clear();
    }

    public void toggleFlashlight(){
        flashlightEnabled = !flashlightEnabled;
    }

    public boolean isFlashlightEnabled(){
        return flashlightEnabled;
    }
    public boolean isSoundOn(){return soundOn;}

    public void setSoundOn(boolean soundOn){this.soundOn=soundOn;}
}
