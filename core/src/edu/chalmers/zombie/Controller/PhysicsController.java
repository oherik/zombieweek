package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.model.Room;
import edu.chalmers.zombie.utils.Constants;

import java.awt.*;
import java.util.ArrayList;

/**
 * This controller class handles the various instantiations of physics objects for any given world.
 * Created by Erik
 * Modified by Erik
 */
public class PhysicsController {

    /**
     * Saves the collision objects
     * @param collisionObjects  A list of collision objects
     */
    public static void setCollisionObjects(ArrayList<CollisionObject> collisionObjects){
        GameModel.getInstance().setCollisionObjects(collisionObjects);
    }

    /**
     * Saves the collision objects with default collision objects
     */
    public static void setCollisionObjects(){
        GameModel.getInstance().setCollisionObjects(createCollisionObjects());
    }
    /**
     * Creates the different collision objects (see CollisionObject.java) that represent the physical world and stores
     * them in the model.
     * @return A list of the collision objects
     */
    private static ArrayList<CollisionObject> createCollisionObjects(){
        //Create a new ArrayList to store the objects
        ArrayList<CollisionObject> collisionObjects = new ArrayList<CollisionObject>();

        //Water, sensor
        short categoryBits = Constants.COLLISION_WATER;
        short maskBits= Constants.COLLISION_ENTITY | Constants.COLLISION_PROJECTILE;
        ZWBody waterSensorBody = new ZWBody();
        waterSensorBody.setFixtureDef(0, 0.1f, 1f, 1f, categoryBits, maskBits, true);
        collisionObjects.add(new CollisionObject(Constants.COLLISION_PROPERTY_WATER, waterSensorBody));

        //Water, collision
        categoryBits= Constants.COLLISION_WATER;
        maskBits= Constants.COLLISION_ZOMBIE;
        ZWBody waterCollisionBody = new ZWBody();
        waterCollisionBody.setFixtureDef(0, 0.1f, 1f, 1f, categoryBits, maskBits, false);
        collisionObjects.add(new CollisionObject(Constants.COLLISION_PROPERTY_WATER, waterCollisionBody));

        //Collision for all
        categoryBits= Constants.COLLISION_OBSTACLE;
        maskBits = Constants.COLLISION_ENTITY | Constants.COLLISION_PROJECTILE;
        ZWBody allCollisionBody = new ZWBody();
        allCollisionBody.setFixtureDef(0, 0.2f, 1f, 1f, categoryBits, maskBits, false);
        collisionObjects.add(new CollisionObject(Constants.COLLISION_PROPERTY_ALL, allCollisionBody));

        //Door
        categoryBits = Constants.COLLISION_DOOR;
        maskBits = Constants.COLLISION_ENTITY | Constants.COLLISION_PROJECTILE;
        ZWBody doorBody = new ZWBody();
        doorBody.setFixtureDef(0, 0.1f, 1f, 1f, categoryBits, maskBits, false);
        collisionObjects.add(new CollisionObject(Constants.COLLISION_PROPERTY_DOOR, doorBody));

        //Sneak, sensor
        categoryBits = Constants.COLLISION_SNEAK;
        maskBits = Constants.COLLISION_PLAYER;
        ZWBody sneakSensorBody = new ZWBody();
        sneakSensorBody.setFixtureDef(0, 0.1f, 1f, 1f, categoryBits, maskBits, true);
        collisionObjects.add(new CollisionObject(Constants.COLLISION_PROPERTY_SNEAK,sneakSensorBody));

        //Sneak, collision
        categoryBits = Constants.COLLISION_SNEAK;
        maskBits = Constants.COLLISION_ZOMBIE;
        ZWBody sneakCollisionBody = new ZWBody();
        sneakCollisionBody.setFixtureDef(0, 0.1f, 1f, 1f, categoryBits, maskBits, false);
        collisionObjects.add(new CollisionObject(Constants.COLLISION_PROPERTY_SNEAK,sneakCollisionBody));


        //Player collision
        categoryBits = Constants.COLLISION_ACTOR_OBSTACLE;
        maskBits = Constants.COLLISION_ENTITY | Constants.COLLISION_PROJECTILE;
        ZWBody playerCollisionBody = new ZWBody();
        playerCollisionBody.setFixtureDef(0, 0.1f, 1f, 1f, categoryBits, maskBits, false);
        collisionObjects.add(new CollisionObject(Constants.COLLISION_PROPERTY_PLAYER, playerCollisionBody));


        return collisionObjects;

    }

    /**
     * This function creates all the Box2d obstacle bodies. The obstacles are the CollisionObjects defined in
     * initializeCollisionObjects() and stored in GameModel.  An obstacle might be a wall, a river or anything else that
     * the player, zombie or projectile should be able to collide with one way or another. The boxes are created by the
     * definitions stored in the collision object array list.
     *
     *
     * It goes through all the tiles in the map looking for tiles containing any of the collision names in the
     * collision object array list. If one is found a box2d fixture is placed there, which allows for collision detection.
     * The fixture then has its user data set to a refernce to the object in question, so it can be called upon
     * during the collision detection.
     *
     * If a tile is found where a door should be placed the door property is stored as a property in the door collision
     * object. The door property is the room which should be loaded if the player touches the door. A new door collision
     * object is then added to the collision object array list, in case any more door is found (the same can't be re-used
     * since the property of the doors in most cases are unique). The door is then removed from the array list.
     *
     * If a collision_all or collision_zombie property isn't found the specific tile is added to the room's zombie navigational mesh
     *
     * @param collisionObjects the list of all the collision objects that can be placed in the world
     */

    private static void traverseRoomIfNeeded(ArrayList<CollisionObject> collisionObjects, Room room) {
        if(!room.hasBeenTraversed()) { //if the room already has these initialized there's no point in continuing
            Room currentRoom = GameModel.getInstance().getRoom();

            String zombieSpawn = "zombie_spawn"; //TODO test tills vi f�r flera sorters zombies
            String playerSpawn = "player_spawn"; //TODO test tills ovan �r fixat
            String playerReturn = "player_return"; //TODO test tills ovan �r fixat

                for (int row = 0; row < room.getTiledHeight(); row++) {       //TODO on�digt att g� igenom allt?
                    for (int col = 0; col < room.getTiledWidth(); col++) {
                        if (room.hasMetaData(col, row)) {        //There's a meta data tile on that position
                            CollisionObject toAdd = null;
                            CollisionObject toRemove = null;
                            for (CollisionObject obj : collisionObjects) {
                                if (room.hasProperty(col, row, obj.getName())){
                                    obj.getBody().setBodyDefPosition((col + 0.5f), (row + 0.5f));
                                    if(obj.getName().equals(Constants.COLLISION_PROPERTY_DOOR)){
                                        toAdd = obj.clone();
                                        toRemove = obj;
                                        obj.setProperty((String) room.getProperty(col, row, Constants.COLLISION_PROPERTY_DOOR));
                                    }
                                    room.createFixture(obj.getBody(), obj);
                                }
                            }
                            if(toRemove != null) {
                                collisionObjects.remove(toRemove);
                            }
                            if(toAdd != null) {
                                collisionObjects.add(toAdd);
                            }
                            if (room.hasProperty(col, row, zombieSpawn)) {           //TODO skapa en spawnEntities-metod ist�llet. Och en huvudmetod som g�r igenom b�da metoderna
                                ZombieController.spawnZombie((String) room.getProperty(col, row, zombieSpawn), col, row);
                            }
                            else if (room.hasProperty(col, row, playerSpawn)) {
                                room.setPlayerSpawn(new Point(col, row));
                            }
                            else if (room.hasProperty(col, row, playerReturn)) {
                                room.setPlayerReturn(new Point(col, row));
                            }
                            else if (room.hasProperty(col, row, Constants.POTION_PROPERTY)) {
                                EntityController.spawnPotion((String)room.getProperty(col, row, Constants.POTION_PROPERTY), room, col, row);
                            }

                            else if (room.hasProperty(col, row, Constants.BOOK_PROPERTY)) {
                                int amount = Integer.parseInt((String)room.getProperty(col, row, Constants.BOOK_PROPERTY));
                                for(int i = 0 ; i<amount; i++){
                                    EntityController.spawnBook(room, col, row);
                                }
                            }

                        }


                        /* ------ Create book obstacles -----*/
                        if (room.hasProperty(col,row,Constants.COLLISION_PROPERTY_ALL)){
                                room.addCollision(col, row, Constants.COLLISION_OBSTACLE);
                            }
                            if(room.hasProperty(col,row,Constants.COLLISION_PROPERTY_ZOMBIE) ||
                                    room.hasProperty(col,row,Constants.COLLISION_PROPERTY_PLAYER)){
                                room.addCollision(col, row, Constants.COLLISION_ACTOR_OBSTACLE);
                            }

                        }
                    }
                }
        room.setHasBeenTraversed(true);

    }



    /**
     * Runs traverseRoomIfNeeded using the default values stored in the game model. If none are stored new ones are created.
     */
    public static void traverseRoomIfNeeded(Room room) {
        if(GameModel.getInstance().getCollisionObjects()==null) {
            setCollisionObjects(createCollisionObjects());
        }
        traverseRoomIfNeeded(GameModel.getInstance().getCollisionObjects(), room);
    }

}
