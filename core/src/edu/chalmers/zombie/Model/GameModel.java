package edu.chalmers.zombie.model;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.utils.GameState;
import edu.chalmers.zombie.utils.ResourceManager;
import edu.chalmers.zombie.adapter.ZWRenderer;

import java.awt.*;
import java.util.*;

/** Stores the game data. The model implements the singleton pattern
 * Created by Tobias on 15-04-02.
 * Modified by Erik
 */
public class GameModel {

    private static GameModel instance = new GameModel();
    public static ResourceManager res;
    private Player player;
    private int currentLevel, currentRoom, highestCompletedLevel, highestCompletedRoom;
    private ArrayList<Room> rooms;
    private ArrayList<Level> levels;
    private ArrayList<Book> books = new ArrayList<Book>();
    private ArrayList<Grenade> grenades = new ArrayList<Grenade>();
    private ArrayList<CollisionObject> collisionObjects;
    private boolean worldNeedsUpdate, stepping, flashlightEnabled = false, soundOn;
    private Map entitiesToRemove;
    private Point playerBufferPosition; //Can't alter the player position directly in the world step
    private GameState gameState; //the state of the game
    private ZWRenderer ZWRenderer;
    private ScreenModel screenModel;

    /**
     * Initializes the game model
     */
    private GameModel(){
        currentRoom = 0;   //TODO test
        res = new ResourceManager();
        stepping=false;
        rooms = new ArrayList<Room>();
        entitiesToRemove = new HashMap<Room, ArrayList<Entity>>();
        worldNeedsUpdate = true;
        soundOn = true;
        screenModel = new ScreenModel();
        initializePlayerTextures();
        initializePotionTextures();
        initializeZombieTextures();
        initializeProjectileTextures();
        initializeSounds();
        initializeRooms();
       }

    private void initializeRooms(){ //TODO varifrån ska vi hämta dessa?
        res.loadTiledMap("room0", "core/assets/Map/Level_1_room_1.tmx");
        res.loadTiledMap("room1", "core/assets/Map/Level_1_room_2.tmx");
        res.loadTiledMap("room2", "core/assets/Map/Test_world_2_next.tmx");

        addRoom(new Room(res.getTiledMap("room0"))); //0
        addRoom(new Room(res.getTiledMap("room1"))); //1
        addRoom(new Room(res.getTiledMap("room2"))); //2
    }

    private void initializeZombieTextures(){
        res.loadTexture("zombie-still", "core/assets/Images/zombie-still.png");
        res.loadTexture("zombie-dead", "core/assets/Images/zombie-dead.png");
        res.loadTexture("zombie", "core/assets/Images/zombie.png");
        res.loadTexture("zombie-data-still", "core/assets/Images/zombie-data-still.png");
        res.loadTexture("zombie-data-dead", "core/assets/Images/zombie-data-dead.png");
        res.loadTexture("zombie-data", "core/assets/Images/zombie-data.png");
        res.loadTexture("zombie-it-still","core/assets/Images/zombie-it-still.png");
        res.loadTexture("zombie-it-dead", "core/assets/Images/zombie-it-dead.png");
        res.loadTexture("zombie-it", "core/assets/Images/zombie-it.png");
    }

    private void initializePlayerTextures(){
        res.loadTexture("player","core/assets/player_professional_final_version.png");
        res.loadTexture("emilia","core/assets/Images/emilia.png");
        res.loadTexture("emilia-hand","core/assets/Images/emilia-hand.png");
        res.loadTexture("emilia-still","core/assets/Images/emilia-still.png");
    }

    private void initializePotionTextures(){
        res.loadTexture("potion-health", "core/assets/Images/healthpotion.png");
        res.loadTexture("potion-speed", "core/assets/Images/speedpotion.png");
    }

    private void initializeProjectileTextures(){
        res.loadTexture("book", "core/assets/Images/bookSprite.png");
    }

    private void initializeSounds(){
        res.loadSound("throw", "core/assets/Audio/Sound_effects/throw_book.mp3");
        res.loadSound("menu_hover", "core/assets/Audio/Sound_effects/menu_hover.mp3");
        res.loadSound("zombie_hit","core/assets/Audio/Sound_effects/zombie_hit.mp3");
        res.loadSound("pick_up_book","core/assets/Audio/Sound_effects/pick_up_book.mp3");
        res.loadSound("zombie_sleeping","core/assets/Audio/Sound_effects/zombie_sleeping.mp3");
    }

    /**
     * @return  The current instance of the game model
     */
    public static GameModel getInstance( ) {
        return instance;
    }

  
    /**
     * @return The game's renderer
     */
    public ZWRenderer getZWRenderer(){
        return this.ZWRenderer;
    }

    /**
     * Sets the game's renderer
     */

    public void setZWRenderer(ZWRenderer ZWRenderer){this.ZWRenderer = ZWRenderer;}

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

    public ArrayList<Book> getBooks(){
        return getRoom().getBooks();
    }

    public ArrayList<Grenade> getGrenades() {
        return grenades;
    }

    public void addBook(Book book){
        getRoom().addBook(book);
    }

    public void addGrenade(Grenade grenade){
        grenades.add(grenade);
    }

    public void addEntitiesToRemove(Map<Room, ArrayList<Entity>> entitySet){this.entitiesToRemove = entitySet; }

    public void addEntityToRemove(Room room, Entity entity){
        if(entitiesToRemove.get(room) == null){
            entitiesToRemove.put(room, new ArrayList<Entity>());
        }
        ArrayList<Entity> entities = (ArrayList<Entity>) entitiesToRemove.get(room);
        entities.add(entity);
        entitiesToRemove.put(room, entities);
    }

    public void clearEntitiesToRemove(){this.entitiesToRemove.clear();}

    public Map<Room, ArrayList<Entity>> getEntitiesToRemove() {return this.entitiesToRemove; }

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

    public boolean isStepping(){
        return stepping;
    }

    public void setStepping(boolean s){
        this.stepping = s;
    }

    public GameState getGameState(){return gameState;}

    public void setGameState(GameState gameState){this.gameState = gameState;}

    public void setHighestCompletedRoom(int room){this.highestCompletedRoom = room;}

    public int getHighestCompletedRoom(){return highestCompletedRoom;}

    public void clearBookList(){
        getRoom().getBooks().clear();
    }

    public void toggleFlashlight(){
        flashlightEnabled = !flashlightEnabled;
    }

    public boolean isFlashlightEnabled(){
        return flashlightEnabled;
    }
    public boolean isSoundOn(){return soundOn;}

    public void setSoundOn(boolean soundOn){this.soundOn=soundOn;}

    public ScreenModel getScreenModel(){return this.screenModel;}

    public void setScreenModel(ScreenModel screenModel){this.screenModel = screenModel;}



}
