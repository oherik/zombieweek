package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.*;
import edu.chalmers.zombie.model.actors.Player;
import edu.chalmers.zombie.model.actors.Zombie;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.PathAlgorithm;
import edu.chalmers.zombie.utils.SaveLoadGame;
import edu.chalmers.zombie.utils.TileRayTracing;
import edu.chalmers.zombie.adapter.ZWRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * This controller class makes all the different calculations regarding the maps, rooms, worlds and objects in them.
 * Created by Erik
 */
public class MapController {

    /**
     * @param roomIndex the room index that will be accessed
     * @return The room specified by the index
     * @throws  IndexOutOfBoundsException if the user tries to access a room not in range
     */
    public static Room getRoom(int roomIndex){

        GameModel gameModel = GameModel.getInstance();
        int maxSize = gameModel.getRooms().size() -1;
        if(roomIndex<0 ||roomIndex > maxSize)
            throw new IndexOutOfBoundsException("Not a valid room index, must be between " + 0 + " and  " + maxSize);
        return gameModel.getRoom(roomIndex);
    }

    /**
     * @return the current room from the model
     */
    public static Room getRoom(){

        GameModel gameModel = GameModel.getInstance();
        return gameModel.getRoom();
    }


    /**
     * Loads a new level. If the level is in front of the current one the player is placed in the first room. If it's
     * behind the current one she's placed in the last room of that level.
     * @param levelIndex    The level to load
     * @throws IndexOutOfBoundsException if the level index is non-valid
     */
    public static void loadLevel(int levelIndex) throws IndexOutOfBoundsException{
        GameModel gameModel = GameModel.getInstance();
        int maxSize = gameModel.getLevels().size() - 1;
        if (levelIndex < 0 || levelIndex > maxSize){
            throw new IndexOutOfBoundsException("Not a valid room index, must be between " + 0 + " and  " + maxSize);
        }

        int oldIndex = gameModel.getCurrentLevelIndex();
        gameModel.setCurrentLevelIndex(levelIndex);
        int oldRoomIndex = gameModel.getCurrentRoomIndex();
        if(oldIndex>levelIndex) {
            gameModel.setCurrentRoomIndex(gameModel.getLevel(levelIndex).numberOfRooms() - 1);
        } else{
            gameModel.setCurrentRoomIndex(0);
        }

        if (levelIndex>gameModel.getHighestCompletedLevel()){
            gameModel.setHighestCompletedLevel(levelIndex);
        }

        System.out.println("Level change to: " + levelIndex);

        /* ------ Save game ------ */
        SaveLoadGame saveLoadGame = new SaveLoadGame();
        saveLoadGame.saveGame();



        loadRoom(oldIndex, levelIndex,oldRoomIndex, 0);
    }

    /**
     * Loads a room in the same level
     * @param roomIndex The room to load
     */
    public static void loadRoom(int roomIndex){
        int levelIndex = GameModel.getInstance().getCurrentLevelIndex();
        loadRoom(levelIndex,levelIndex, GameModel.getInstance().getCurrentRoomIndex(), roomIndex);
    }


    /**
     * Loads the new room in the game model, creates bodies if needed and sets that the renderer needs to update the
     * world. It also updates where the player will be placed after the world step function (it's not possible to do it
     * at the same time, thus a temporary point is stored in the model). This is decided based on if the room that's
     * being loaded is before or after the one just shown to the user. It then creates the collision bodies for the new
     * room, if needed and sets a variable in the game model that the renderer needs to update the world in the next
     * world step.
     *
     * @param newRoomIndex the room to load
     * @param oldRoomIndex the previous room
     * @param newLevelIndex the level the room is in
     * @param oldLevelIndex the level the old rom is in
     * @throws  IndexOutOfBoundsException if the user tries to access a room not in range
     */
    private static void loadRoom(int oldLevelIndex, int newLevelIndex, int oldRoomIndex, int newRoomIndex) {
        GameModel gameModel = GameModel.getInstance();
        int maxSize = gameModel.getLevel().numberOfRooms() - 1;
        if (newRoomIndex < 0 || newRoomIndex > maxSize){
            throw new IndexOutOfBoundsException("Not a valid room index, must be between " + 0 + " and  " + maxSize);
        }
        //if(!gameModel.worldNeedsUpdate()){
        gameModel.getPlayer().setSneakTilesTouching(0);
        gameModel.getPlayer().setWaterTilesTouching(0);
        gameModel.getPlayer().setHidden(false);
        //TODO sluta simma, sluta sneaka
        EntityController.setFriction(gameModel.getPlayer(), Constants.PLAYER_FRICTION_DEFAULT, Constants.PLAYER_FRICTION_DEFAULT);
        GameModel.getInstance().addEntityToRemove(gameModel.getLevel(oldLevelIndex).getRoom(oldRoomIndex), GameModel.getInstance().getPlayer());

       // for(Book book : gameModel.getBooks()){
       //     book.markForRemoval();
      //      gameModel.addEntityToRemove(getRoom(),book);
      //  }
        gameModel.setCurrentRoomIndex(newRoomIndex);
       // gameModel.clearBookList();
        SpawnController.traverseRoomIfNeeded(getRoom());
        if(oldRoomIndex > newRoomIndex && oldLevelIndex >= newLevelIndex){
            if(getRoom().getPlayerReturn() == null)        //If the spawn and return points are the same point in the map file
                setPlayerBufferPosition(getRoom().getPlayerSpawn());
            else
                setPlayerBufferPosition(getRoom().getPlayerReturn());
        }
        else if(oldLevelIndex > newLevelIndex){
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
    public static boolean worldNeedsUpdate(){
        GameModel gameModel = GameModel.getInstance();
        return gameModel.worldNeedsUpdate();
    }

    /**
     * If the world needs to update the next step, this variable is set in the model
     * @param bool true if the world needs to be updated, false if not
     */
    public static void setWorldNeedsUpdate(boolean bool){
        GameModel gameModel = GameModel.getInstance();
        gameModel.setWorldNeedsUpdate(bool);
    }

    /**
     * @return The player's current (rounded) position as a point
     */
    public static Point getPlayerPosition(){
        GameModel gameModel = GameModel.getInstance();
        return new Point(Math.round(gameModel.getPlayer().getX()), Math.round(gameModel.getPlayer().getY()));
    }

    /**
     * Sets where the player should be when the world step is done
     * @param point Where the player will be placed after the step
     */
    public static void setPlayerBufferPosition(Point point){
        GameModel gameModel = GameModel.getInstance();
        gameModel.setPlayerBufferPosition(point);
    }

    /**
     * @return where the player will be placed after the step
     */
    public static Point getPlayerBufferPosition(){
        GameModel gameModel = GameModel.getInstance();
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
    public static boolean pathObstructed(ZWVector position, Room room, float distance, float angle){
    /* Input checks */
        if(position == null ||room== null)
            throw new NullPointerException("The input mustn't be null");
        if(position.getX() < 0 || position.getY() < 0)
            throw new IndexOutOfBoundsException("The position must be positive");   //TODO kanske inte behövs i och med att det checkas i relevant metod
        if(position.getX() > room.getTiledWidth() || position.getY() >room.getTiledHeight())
            throw new IndexOutOfBoundsException("The position must be within meta layer bounds");//TODO kanske inte behövs i och med att det checkas i relevant metod
        if(distance < 0)
            throw new IndexOutOfBoundsException("The distance must be positive");
        ZWVector endPosition = position.add(distance * (float) Math.cos(angle), distance * (float) Math.sin(angle));
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


    public static void printCollisionTileGrid(){       //TODO debugmetod
        GameModel gameModel = GameModel.getInstance();
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



    public static void printPath(Room room, Point start, Point end) throws NullPointerException, IndexOutOfBoundsException{  //TODO debugmetod
        GameModel gameModel = GameModel.getInstance();
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
    public static void updateRoomIfNeeded() {
        GameModel gameModel = GameModel.getInstance();
        removeEntities();
        if (worldNeedsUpdate()) {
           Room currentRoom = getRoom();

            Player player = GameModel.getInstance().getPlayer();

            /* ------ Update physics ------ */
            SpawnController.traverseRoomIfNeeded(getRoom());

            /* ------ Remove old entities ------ */
           // removeEntities();

            /* ------ Update player ------ */
            if(player == null){
               player = PlayerController.updatePlayer();
            }
            if(player.getBody() == null||!player.getBody().bodyIsInWorld(getRoom().getWorld())){
                System.out.println(getPlayerBufferPosition());

                PlayerController.setWorldAndPosition(currentRoom.getWorld(), (float) getPlayerBufferPosition().getX() + 0.5f, (float) getPlayerBufferPosition().getY() + 0.5f);
            }

            /* ------ Update screen ------ */
            if(gameModel.getZWRenderer() == null)
                gameModel.setZWRenderer(new ZWRenderer(getRoom(), ZWGameEngine.getWindowWidth(), ZWGameEngine.getWindowHeight()));  //TODO ej här!
            gameModel.getZWRenderer().updateRoom(getRoom(), ZWGameEngine.getWindowWidth(), ZWGameEngine.getWindowHeight());          //TODO ej här!

            /* ------ Mark as updated ------ */
            setWorldNeedsUpdate(false);
        }
        /* ------ Update physics ------ */
        stepWorld();



        /* ------- Updates projectiles ------*/
        updateProjectiles();

        if(gameModel.getPlayer()!=null && gameModel.getPlayer().getBody()!=null) {       //Another world is loading

            /* ------ Move player ------*/
            PlayerController.moveIfNeeded();

            /* ------ Move zombies ------ */
            moveZombies();
        }
    }

    /**
     * Updates the physical world
     */
    private static  void stepWorld(){
        GameModel gameModel = GameModel.getInstance();

        /* ------ Step the world, i.e. update the game physics. The model gets a variable set to tell it that the world is stepping and no physic operations should be performed ------ */
        gameModel.setStepping(true);
        getRoom().stepWorld(Constants.TIMESTEP, 6, 2);    //TODO är det floaten ovan som ska vara här?
        gameModel.setStepping(false);
    }

    /**
     * Removes the entity bodies from the world if necessary
     */
    private static void removeEntities(){
        GameModel gameModel = GameModel.getInstance();
        for(Map.Entry<Room, ArrayList<Entity>> e: gameModel.getEntitiesToRemove().entrySet()){
            for(Entity entity : e.getValue()) {
                gameModel.getRoom().destroyBody(entity);
            }
        }
        gameModel.clearEntitiesToRemove();
    }

    /* ------ Make all the zombies move toward the player if appropriate ------ */
    private static void moveZombies() {
        GameModel gameModel = GameModel.getInstance();
        for (Zombie z : gameModel.getZombies()) {
            ZombieController.move(z);
        }
    }

    /**
     * Updates projectiles
     */
    private static void updateProjectiles(){
        updateBooks();
        updateGrenades();
    }

    /**
     * Updates books
     */
    private static void updateBooks(){
        GameModel gameModel = GameModel.getInstance();
        ArrayList<Book> books = gameModel.getBooks();
        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);
            long airTime = 500;
            long lifeTime = 5000; //life time for book in millisec
            if (System.currentTimeMillis() - b.getTimeCreated() > airTime && b.getBody()!=null)
                ProjectileController.hitGround(b);
            if (b.toRemove())
                books.remove(i); //Förenklad forsats skulle göra detta svårt
        }
    }

    /**
     * Updates grenades
     */
    private static void updateGrenades(){
        GameModel gameModel = GameModel.getInstance();
        ArrayList<Grenade> grenades = gameModel.getGrenades();
        for(Grenade g : grenades) {
            if (g.getForce().len() != 0) {
                ProjectileController.stopIfNeeded(g);
            }
        }
    }

    public static void resizeRenderer(int width, int height){
        GameModel.getInstance().getZWRenderer().resizeCamera(width,height);
    }

    public static void setCameraPosition(float x, float y){
        GameModel.getInstance().getZWRenderer().setCameraPosition(x, y);
    }


}
