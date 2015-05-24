package edu.chalmers.zombie.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.adapter.CollisionObject;
import edu.chalmers.zombie.adapter.Player;
import edu.chalmers.zombie.adapter.Room;
import edu.chalmers.zombie.adapter.Zombie;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.testing.ZombieTest;
import edu.chalmers.zombie.utils.Constants;

import java.awt.*;
import java.util.ArrayList;

/**
 * This controller class handles the various instantiations of physics objects for any given world.
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
        //Create and define body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; //The collision objects shouldn't move

        //Define shapes
        PolygonShape standardBoxShape = new PolygonShape();
        standardBoxShape.setAsBox(0.5f, 0.5f);   //The size is set as 2 * the values inside the parantheses
        PolygonShape doorShape = new PolygonShape();    //The door is thinner, so the player doesn't accidentally bump into them
        doorShape.setAsBox(0.25f, 0.5f); //The size is set as 2 * the values inside the parantheses

        //Create a new ArrayList to store the objects
        ArrayList<CollisionObject> collisionObjects = new ArrayList<CollisionObject>();

        //Water, sensor
        FixtureDef fixDef = new FixtureDef();
        fixDef.friction = 0;
        fixDef.restitution = .1f;
        fixDef.shape = standardBoxShape;
        fixDef.filter.categoryBits = Constants.COLLISION_WATER;
        fixDef.filter.maskBits = Constants.COLLISION_ENTITY | Constants.COLLISION_PROJECTILE;
        fixDef.isSensor = true;
        collisionObjects.add(new CollisionObject(Constants.COLLISION_PROPERTY_WATER, bodyDef, fixDef));

        //Water, collision
        fixDef = new FixtureDef();
        fixDef.friction = 0;
        fixDef.restitution = .1f;
        fixDef.shape = standardBoxShape;
        fixDef.filter.categoryBits = Constants.COLLISION_WATER;
        fixDef.filter.maskBits = Constants.COLLISION_ZOMBIE;
        collisionObjects.add(new CollisionObject(Constants.COLLISION_PROPERTY_WATER, bodyDef, fixDef));

        //Collision for all
        fixDef = new FixtureDef();  //Reset the fixture definition, this has to be done for each new object
        fixDef.friction = 0.2f;
        fixDef.restitution = .1f;
        fixDef.shape = standardBoxShape;
        fixDef.filter.categoryBits = Constants.COLLISION_OBSTACLE;
        fixDef.filter.maskBits = Constants.COLLISION_ENTITY | Constants.COLLISION_PROJECTILE;
        collisionObjects.add(new CollisionObject(Constants.COLLISION_PROPERTY_ALL, bodyDef, fixDef));

        //Door
        fixDef = new FixtureDef();;
        fixDef.shape = doorShape;
        fixDef.filter.categoryBits = Constants.COLLISION_DOOR;
        fixDef.filter.maskBits = Constants.COLLISION_ENTITY | Constants.COLLISION_PROJECTILE;
        collisionObjects.add(new CollisionObject(Constants.DOOR_PROPERTY, bodyDef, fixDef));

        //Sneak, sensor
        fixDef = new FixtureDef();
        fixDef.friction = 0f;
        fixDef.restitution = .1f;
        fixDef.shape = standardBoxShape;
        fixDef.filter.categoryBits = Constants.COLLISION_SNEAK;
        fixDef.filter.maskBits = Constants.COLLISION_PLAYER;
        fixDef.isSensor = true;
        collisionObjects.add(new CollisionObject(Constants.COLLISION_PROPERTY_SNEAK, bodyDef, fixDef));

        //Sneak, collision
        fixDef = new FixtureDef();
        fixDef.friction = 0f;
        fixDef.restitution = .1f;
        fixDef.shape = standardBoxShape;
        fixDef.filter.categoryBits = Constants.COLLISION_SNEAK;
        fixDef.filter.maskBits = Constants.COLLISION_ZOMBIE;
        collisionObjects.add(new CollisionObject(Constants.COLLISION_PROPERTY_SNEAK, bodyDef, fixDef));


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

    private static void createBodiesIfNeeded(ArrayList<CollisionObject> collisionObjects, Room room) {
        if(!room.hasInitializedBodies()) { //if the room already has these initialized there's no point in continuing
            World world = room.getWorld();
            TiledMapTileLayer metaLayer = room.getMetaLayer();

            String zombieSpawn = "zombie_spawn"; //TODO test tills vi får flera sorters zombies
            String playerSpawn = "player_spawn"; //TODO test tills ovan är fixat
            String playerReturn = "player_return"; //TODO test tills ovan är fixat

            if (metaLayer != null) {
                metaLayer.setVisible(false);
                for (int row = 0; row < metaLayer.getHeight(); row++) {       //TODO onödigt att gå igenom allt?
                    for (int col = 0; col < metaLayer.getWidth(); col++) {
                        TiledMapTileLayer.Cell currentCell = metaLayer.getCell(col, row);
                        if (currentCell != null && currentCell.getTile() != null) {        //There's a meta data tile on that position
                            CollisionObject toAdd = null;
                            CollisionObject toRemove = null;
                            for (CollisionObject obj : collisionObjects) {
                                if (currentCell.getTile().getProperties().get(obj.getName()) != null) {
                                    obj.getBodyDef().position.set((col + 0.5f), (row + 0.5f));
                                    if(obj.getName().equals(Constants.DOOR_PROPERTY)){
                                        toAdd = obj.clone();
                                        toRemove = obj;
                                        obj.setProperty((String) currentCell.getTile().getProperties().get(Constants.DOOR_PROPERTY));
                                    }
                                    Fixture fixture = world.createBody(obj.getBodyDef()).createFixture(obj.getFixtureDef());
                                    fixture.setUserData(obj);
                                }
                            }
                            if(toRemove != null) {
                                collisionObjects.remove(toRemove);
                            }
                            if(toAdd != null) {
                                collisionObjects.add(toAdd);
                            }
                            if (currentCell.getTile().getProperties().get(zombieSpawn) != null) {           //TODO skapa en spawnEntities-metod istället. Och en huvudmetod som går igenom båda metoderna
                                Zombie zombie = new ZombieTest(world, col, row);           //TODO test
                                room.addZombie(zombie);
                            }
                            if (currentCell.getTile().getProperties().get(playerSpawn) != null) {
                                room.setPlayerSpawn(new Point(col, row));
                                GameModel.getInstance().setPlayer(new Player(GameModel.getInstance().res.getTexture("emilia"), world, col, row)); //TODO test
                            }
                            if (currentCell.getTile().getProperties().get(playerReturn) != null) {
                                room.setPlayerReturn(new Point(col, row));
                            }
                        }


                        /* ------ Create book obstacles -----*/
                        if (currentCell != null && currentCell.getTile() != null){
                            if(currentCell.getTile().getProperties().get(Constants.COLLISION_PROPERTY_ALL)!= null) {
                                room.addCollision(col, row, Constants.COLLISION_OBSTACLE);
                            }
                            if(currentCell.getTile().getProperties().get(Constants.COLLISION_PROPERTY_ZOMBIE) != null){
                                room.addCollision(col, row, Constants.COLLISION_ZOMBIE);
                            }

                        }
                    }
                }
            }
            room.setInitializedBodies(true);
        }
    }

    /**
     * Runs createBodiesIfNeeded using the default values stored in the game model. If none are stored new ones are created.
     */
    public static void createBodiesIfNeeded(Room room) {
        if(GameModel.getInstance().getCollisionObjects()==null) {
            setCollisionObjects(createCollisionObjects());
        }
        createBodiesIfNeeded(GameModel.getInstance().getCollisionObjects(), room);
    }

}
