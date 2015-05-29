package edu.chalmers.zombie.controller;

import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.maps.tiled.TiledMap;
//mport com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
//import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.PathAlgorithm;
import edu.chalmers.zombie.utils.TileRayTracing;
import edu.chalmers.zombie.view.GameScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * This controller class makes all the different calculations regarding the maps, rooms, worlds and objects in them.
 */
public class MapController {
    GameModel gameModel;

    /**
     * Constructor
     */
    public MapController(){
        this.gameModel = GameModel.getInstance();
    }



    /**
     * @param roomIndex the room index that will be accessed
     * @return The room specified by the index
     * @throws  IndexOutOfBoundsException if the user tries to access a room not in range
     */
    public Room getRoom(int roomIndex){
        int maxSize = gameModel.getRooms().size() -1;
        if(roomIndex<0 ||roomIndex > maxSize)
            throw new IndexOutOfBoundsException("Not a valid room index, must be between " + 0 + " and  " + maxSize);
        return gameModel.getRoom(roomIndex);
    }

    /**
     * @return the current room from the model
     */
    public Room getRoom(){return gameModel.getRoom();}

    /**
     * Creates the different rooms and stores them in the model
     */

    public void initializeRooms(){ //TODO varifrån ska vi hämta dessa?
        gameModel.res.loadTiledMap("room0", "core/assets/Map/Level_1_room_1.tmx");
        gameModel.res.loadTiledMap("room1", "core/assets/Map/Test_world_3.tmx");
        gameModel.res.loadTiledMap("room2", "core/assets/Map/Test_world_2_next.tmx");

        gameModel.addRoom(new Room(gameModel.res.getTiledMap("room0"))); //0
        gameModel.addRoom(new Room(gameModel.res.getTiledMap("room1"))); //1
        gameModel.addRoom(new Room(gameModel.res.getTiledMap("room2"))); //2
    }


    /**
     * Loads the new room in the game model, creates bodies if needed and sets that the renderer needs to update the
     * world. It also updates where the player will be placed after the world step function (it's not possible to do it
     * at the same time, thus a temporary point is stored in the model). This is decided based on if the room that's
     * being loaded is before or after the one just shown to the user. It then creates the collision bodies for the new
     * room, if needed and sets a variable in the game model that the renderer needs to update the world in the next
     * world step.
     *
     * @param roomIndex the room to load
     * @throws  IndexOutOfBoundsException if the user tries to access a room not in range
     */
    public void loadRoom(int roomIndex) {
        int maxSize = gameModel.getRooms().size() - 1;
        if (roomIndex < 0 || roomIndex > maxSize){
        throw new IndexOutOfBoundsException("Not a valid room index, must be between " + 0 + " and  " + maxSize);
        }
        //if(!gameModel.worldNeedsUpdate()){
        gameModel.getPlayer().setSneakTilesTouching(0);
        gameModel.getPlayer().setWaterTilesTouching(0);
        gameModel.getPlayer().setHidden(false);
        //TODO sluta simma, sluta sneaka
        EntityController.setFriction(gameModel.getPlayer(), Constants.PLAYER_FRICTION_DEFAULT, Constants.PLAYER_FRICTION_DEFAULT);
        int oldRoomIndex = gameModel.getCurrentRoomIndex();

        GameModel.getInstance().addEntityToRemove(getRoom(),GameModel.getInstance().getPlayer());
        for(Book book : gameModel.getBooks()){
            book.markForRemoval();
            gameModel.addEntityToRemove(getRoom(),book);
        }
        gameModel.setCurrentRoomIndex(roomIndex);
        gameModel.clearBookList();
        PhysicsController.traverseRoomIfNeeded(getRoom());
        if(oldRoomIndex>roomIndex){
            if(getRoom().getPlayerReturn() == null)        //If the spawn and return points are the same point in the map file
                setPlayerBufferPosition(getRoom().getPlayerSpawn());
            else
                setPlayerBufferPosition(getRoom().getPlayerReturn());
        }
        else
            setPlayerBufferPosition(getRoom().getPlayerSpawn());

        gameModel.setWorldNeedsUpdate(true);
    }

    /**
     * @return true if the world needs to be updated, false if not
     */
    public boolean worldNeedsUpdate(){
        return gameModel.worldNeedsUpdate();
    }

    /**
     * If the world needs to update the next step, this variable is set in the model
     * @param bool true if the world needs to be updated, false if not
     */
    public void setWorldNeedsUpdate(boolean bool){
        gameModel.setWorldNeedsUpdate(bool);
    }

    /**
     * @return The player's current (rounded) position as a point
     */
    public Point getPlayerPosition(){
        return new Point(Math.round(gameModel.getPlayer().getX()), Math.round(gameModel.getPlayer().getY()));
    }

    /**
     * Updates the player's position
     * @param point Where the player will be placed
     */
    public void updatePlayerPosition(Point point){
               gameModel.getPlayer().setPosition(point);
    }

    /**
     * Sets where the player should be when the world step is done
     * @param point Where the player will be placed after the step
     */
    public void setPlayerBufferPosition(Point point){
        gameModel.setPlayerBufferPosition(point);
    }

    /**
     * @return where the player will be placed after the step
     */
    public Point getPlayerBufferPosition(){
        return gameModel.getPlayerBufferPosition();
    }

    /**
     * Checks if the path is obstructed by a wall using a modified version of Bresenham's line algorithm while taking into account how the maps are constructed.
     * @param position  The original position
     * @param room The map
     * @param distance  The distance in which to check
     * @param angle the angle to check, in radians (0 is east, pi is west)
     * @return  true if the path is obstructed, false otherwise
     */
    public static boolean pathObstructed(Vector position, Room room, float distance, float angle){
    /* Input checks */
        if(position == null ||room== null)
            throw new NullPointerException("The input mustn't be null");
        if(position.getX() < 0 || position.getY() < 0)
            throw new IndexOutOfBoundsException("The position must be positive");   //TODO kanske inte behövs i och med att det checkas i relevant metod
        if(position.getX() > room.getTiledWidth() || position.getY() >room.getTiledHeight())
            throw new IndexOutOfBoundsException("The position must be within meta layer bounds");//TODO kanske inte behövs i och med att det checkas i relevant metod
        if(distance < 0)
            throw new IndexOutOfBoundsException("The distance must be positive");
        Vector endPosition = position.add(distance * (float) Math.cos(angle), distance * (float) Math.sin(angle));
        /* Extract and convert the positions to map coordinates */
        int x_origin = Math.round(position.getX() - position.getX() % 1 - 0.5f);
        int y_origin = Math.round(position.getY() - position.getY() % 1 - 0.5f);
        int x_end = Math.round(endPosition.getX() - endPosition.getX() % 1 - 0.5f);
        int y_end = Math.round(endPosition.getY() - endPosition.getY() % 1 - 0.5f);

        return TileRayTracing.pathObstructed(new Point(x_origin, y_origin), new Point(x_end, y_end), room.getCollisionTileGrid(), Constants.COLLISION_OBSTACLE);
    }


    /**
     * @return The current room's zombie navigation mesh
     */
    public short[][] getCollisionTileGrid(){
        return getRoom().getCollisionTileGrid();

    }


    public void printCollisionTileGrid(){       //TODO debugmetod
        System.out.println("\nRoom nr " + (gameModel.getCurrentRoomIndex()+1) +": printing collision detection tiles.");
        System.out.println("Width: " + getRoom().getCollisionTileGrid().length    + " Height: " + getRoom().getCollisionTileGrid()[0].length);
        for(int y = getRoom().getCollisionTileGrid()[0].length-1; y >= 0; y--){
            for(int x = 0; x < getRoom().getCollisionTileGrid().length; x++){
                if(getRoom().getCollisionTileGrid()[x][y] == 0)
                    System.out.print("\t ");
                else
                    System.out.print("\t" + getRoom().getCollisionTileGrid()[x][y]);
            }
            System.out.println("");
        }
    }



    public void printPath(Room room, Point start, Point end) throws NullPointerException, IndexOutOfBoundsException{  //TODO debugmetod
            ArrayList<Point> path = getPath(room, start, end, Constants.MAX_PATH_COST);
            System.out.println("\nRoom nr " + (gameModel.getCurrentRoomIndex()+1) +
                    ": printing collision detection tiles and path from " + start.x + ", " + start.y + " to " + end.x + ", " + end.y + ".");
        if(path != null) {
            System.out.println("Width: " + getRoom().getCollisionTileGrid().length + " Height: " + getRoom().getCollisionTileGrid()[0].length);
            for (int y = getRoom().getCollisionTileGrid()[0].length - 1; y >= 0; y--) {
                for (int x = 0; x < getRoom().getCollisionTileGrid().length; x++) {
                    Point point = new Point(x, y);
                    if (getRoom().getCollisionTileGrid()[x][y] == 0 && !path.contains(point)) {
                        System.out.print("\t ");
                    } else if (path.contains(point)) {
                        if (point.equals(start)) {
                            System.out.print("\t o");
                        } else if(point.equals(end)) {
                            System.out.print("\t x");
                        } else {
                            System.out.print("\t -");
                        }
                    } else {
                        System.out.print("\t" + getRoom().getCollisionTileGrid()[x][y]);
                    }
                }
                System.out.println("");
            }
            System.out.print("Points in path:\n");
            for (Point p : path) {
                System.out.println(p.x + ", " + p.y);
            }
            System.out.print("Number of points: " + path.size());
        }
        else System.out.print("No path found");

    }

    /**
     * Returns the shortest path between two points. Takes obstacles into account. Since the algorithm is layout a bit different from the map tiles, 1 must be subtracted from the x and y values.
     * @param room  The specific room
     * @param start The start point
     * @param end   The end point
     * @param maxSteps The maximum path length
     * @return  The shortest path between the two points in the room
     * @throws NullPointerException if either parameter is null or of the path algorithm or navigational mesh haven't been initialized
     * @throws IndexOutOfBoundsException if any point is out of bounds
     */
    public static ArrayList<Point> getPath(Room room, Point start, Point end, int maxSteps) throws NullPointerException, IndexOutOfBoundsException{
        if(room==null){
            throw new NullPointerException("the room pointer was null");
        }
        if(room.getCollisionTileGrid()==null){
            throw new NullPointerException("getPath: the room's navigation mesh was null, can't create path without one");
        }
        if(start==null ||end == null){
            throw new NullPointerException("getPath: The points mustn't be null");
        }
        if(start.x < 0 || start.x >= room.getCollisionTileGrid().length || start.y < 0 ||start.y >= room.getCollisionTileGrid()[0].length)
            throw new IndexOutOfBoundsException("getPath: start point out of bounds");
        if(end.x < 0 || end.x >= room.getCollisionTileGrid().length || end.y < 0 ||end.y >= room.getCollisionTileGrid()[0].length)
            throw new IndexOutOfBoundsException("getPath: end point out of bounds");
        if(maxSteps<1)
            throw new IndexOutOfBoundsException("getPath: max steps must be >0");
        return PathAlgorithm.getPath(start, end, room.getCollisionTileGrid(), maxSteps, Constants.COLLISION_ACTOR_OBSTACLE);
    }

    /**
     *
     * Return the shortest path between two points in the current room
     * @param start The start point
     * @param end   The end point
     * @param maxSteps The maximum length of the path
     * @return  The shortest path between the two points in the current room
     * @throws NullPointerException if either parameter is null or of the path algorithm or navigational mesh haven't been initialized
     *      * @throws IndexOutOfBoundsException if any point is out of bounds
     */
    public static ArrayList<Point> getPath(Point start, Point end, int maxSteps) throws NullPointerException, IndexOutOfBoundsException {
        MapController controller = new MapController(); //TODO gör de andra statiska
        return getPath(controller.getRoom(), start, end, maxSteps);
    }

    /**
     * If the room has changed the map and renderer need to change as well
     */
    public void updateRoomIfNeeded() {
        removeEntities();
        if (worldNeedsUpdate()) {
           Room currentRoom = getRoom();

            Player player = GameModel.getInstance().getPlayer();

            /* ------ Update physics ------ */
            PhysicsController.traverseRoomIfNeeded(getRoom());

            /* ------ Remove old entities ------ */
           // removeEntities();

            /* ------ Update player ------ */
            if(player == null){
               player = EntityController.createNewPlayer();
            }
            if(player.getBody() == null||player.getBody().getWorld()!=getRoom().getWorld()){
                System.out.println(getPlayerBufferPosition());
                player.createDefaultBody(currentRoom.getWorld(), getPlayerBufferPosition());
            }

            /* ------ Update screen ------ */
            if(gameModel.getRenderer() == null)
                gameModel.setRenderer(new Renderer(getRoom(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));  //TODO ej här!
            gameModel.getRenderer().updateRoom(getRoom(),  Gdx.graphics.getWidth(), Gdx.graphics.getHeight());          //TODO ej här!

            /* ------ Save game ------ */
            SaveLoadController saveLoadController = new SaveLoadController();
            saveLoadController.saveGame();

            /* ------ Mark as updated ------ */
            setWorldNeedsUpdate(false);

            //TODO debug
            printCollisionTileGrid();
        }
        /* ------ Update physics ------ */
        stepWorld();



        /* ------- Updates projectiles ------*/
        updateBooks();

        if(gameModel.getPlayer()!=null && gameModel.getPlayer().getBody()!=null) {       //Another world is loading

            /* ------ Move player ------*/
            gameModel.getPlayer().moveIfNeeded();

            /* ------ Move zombies ------ */
            moveZombies();
        }
    }

    /**
     * Updates the physical world
     */
    private void stepWorld(){

        /* ------ Step the world, i.e. update the game physics. The model gets a variable set to tell it that the world is stepping and no physic operations should be performed ------ */
        gameModel.setStepping(true);
        getRoom().stepWorld(Constants.TIMESTEP, 6, 2);    //TODO är det floaten ovan som ska vara här?
        gameModel.setStepping(false);
    }

    /**
     * Removes the entity bodies from the world if necessary
     */
    private void removeEntities(){
        for(Map.Entry<Room, ArrayList<Entity>> e: gameModel.getEntitiesToRemove().entrySet()){
            for(Entity entity : e.getValue()) {
                gameModel.getRoom().destroyBody(entity);
            }
        }
        gameModel.clearEntitiesToRemove();
    }

    /* ------ Make all the zombies move toward the player if appropriate ------ */
    private void moveZombies() {
        for (Zombie z : gameModel.getZombies()) {
            ZombieController.move(z);
        }
    }

    /**
     * Updates projectiles
     */
    private void updateBooks(){
        ArrayList<Book> books = gameModel.getBooks();
        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);
            long airTime = 500;
            long lifeTime = 5000; //life time for book in millisec
            if (System.currentTimeMillis() - b.getTimeCreated() > airTime && b.getBody()!=null)
                EntityController.hitGround(b);
            if (System.currentTimeMillis() - b.getTimeCreated() > lifeTime) {
              //  gameModel.addEntityToRemove(getRoom(),b);
                //b.markForRemoval(); //TODO ha med?
            }
            if (b.toRemove())
                books.remove(i); //Förenklad forsats skulle göra detta svårt
        }
    }

}
