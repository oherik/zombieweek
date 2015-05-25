package edu.chalmers.zombie.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;

/** A custom contact listener. It registers the different contacts and forward the commands to the other controllers.
 * Created by Erik on 2015-04-18.
 */
public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
    private GameModel gameModel;
    private MapController mapController;
    private EntityController entityController;

    /**
     * Instantiates the contact listener.
     */
    public ContactListener (){
        this.gameModel = GameModel.getInstance();
        this.mapController = new MapController();
        this.entityController = new EntityController();
    }

    /**
     * This method decides what to do when two objects start colliding. Even though its name is ContactListener, not
     * ContactController, it is in all regards a controller class and has thus been placed in the Controller package
     * instead of in Utils
     *
     * There are several different contacts we are interested in. The first is if a book has struck a zombie. The second is
     * if the player collides with a book, which then, if appropriate, should be picked up. The third is if the player has
     * made contact with a door.
     *
     *  Regarding the book (and other entities), a reference the Book object is stored as user data in its body.
     *  Therefore, if we are certain the object is a book, we can call on the user data to retrieve it.
     *
     *  If the book has hit the player the book is marked for removal (the body can't be removed during the world step,
     *  which is why it's done this way) and the model is notified of this. The player ammunition is increased. If the
     *  book has hit a zombie the book is again marked for removal and the Zombie is marked as knocked out. Both entities
     *  are added to the entities to be removed list. If the book hits the wall friction is applied, to give the illusion
     *  that the book is now on the ground
     *
     * The next checks are too see if the player makes contact with a door. Due to the way Box2D is made we have to check for
     * both cases - either the player makes contact with a door or a door makes contact with the Player. This might seem to
     * be the same thing, but it's handled differently in Box2d. If the player has indeeed made contact with a door the
     * appropriate level is loaded via the map controller.
     *
     * @param contact   The contact object between two objects
     */

    public void beginContact (Contact contact) {
        gameModel.clearEntitiesToRemove();
        switch(contact.getFixtureB().getFilterData().categoryBits) {
            case Constants.COLLISION_PROJECTILE://Check if the fixture is a projectile, e.g. a book
                Book b = (Book) contact.getFixtureB().getBody().getUserData();                                  //Retrieve the book
                switch(contact.getFixtureA().getFilterData().categoryBits){
                    case Constants.COLLISION_PLAYER:
                        Player p = gameModel.getPlayer();
                        EntityController.pickUp(p, b);
                        break;
                    case Constants.COLLISION_ZOMBIE:
                        Zombie z = (Zombie) contact.getFixtureA().getBody().getUserData();
                        EntityController.applyHit(z, b);
                        break;
                    case Constants.COLLISION_OBSTACLE:
                        EntityController.hitGround(b);
                        break;
                    case Constants.COLLISION_WATER:
                        //TODO plums
                        EntityController.remove(b);
                        break;
                }
              break;
            case (Constants.COLLISION_WATER):
                switch(contact.getFixtureA().getFilterData().categoryBits){
                    case Constants.COLLISION_PLAYER:
                        //TODO Ner i vatten
                        gameModel.getPlayer().setWaterTilesTouching(gameModel.getPlayer().getWaterTilesTouching()+1);
                        EntityController.setFriction(gameModel.getPlayer(), Constants.PLAYER_FRICTION_WATER, Constants.PLAYER_FRICTION_WATER);
                        break;
                }
        }
    }

    /**
     * Called when two objects stop colliding.
     * @param contact The contact object between two objects
     */
    public void endContact (Contact contact){
        switch(contact.getFixtureB().getFilterData().categoryBits) {
            case (Constants.COLLISION_WATER):
                switch (contact.getFixtureA().getFilterData().categoryBits) {        //Not made as an if-statement if more collision alternatives are to be added
                    case Constants.COLLISION_PLAYER:
                        Player player = gameModel.getPlayer();
                        player.setWaterTilesTouching(player.getWaterTilesTouching() - 1);
                        if(player.getWaterTilesTouching()<1) {
                            //TODO upp ur vattnet
                            EntityController.setFriction(player, Constants.PLAYER_FRICTION_DEFAULT, Constants.PLAYER_FRICTION_DEFAULT);
                        }
                        break;
                }
        }
     }

    /**
     * Not used in the current version
     * @param contact The contact object between two objects
     */
    public  void preSolve(Contact contact, Manifold manifold){
        switch(contact.getFixtureB().getFilterData().categoryBits) {
            case (Constants.COLLISION_PLAYER):
                switch (contact.getFixtureA().getFilterData().categoryBits){        //Not made as an if-statement if more collision alternatives are to be added
                    case Constants.COLLISION_DOOR:
                             CollisionObject door = (CollisionObject) contact.getFixtureA().getUserData();
                            int levelToLoad = Integer.parseInt(door.getProperty());
                          mapController.loadLevel(levelToLoad);
                        break;

                }
                break;
            case (Constants.COLLISION_DOOR):
                switch(contact.getFixtureA().getFilterData().categoryBits){
                    case Constants.COLLISION_PLAYER:
                            CollisionObject door = (CollisionObject) contact.getFixtureB().getUserData();
                          int levelToLoad = Integer.parseInt(door.getProperty());
                         mapController.loadLevel(levelToLoad);
                        break;
                }
                break;
        }
    }
    /**
     * Not used in the current version
     * @param contact The contact object between two objects
     */
    public  void postSolve(Contact contact,ContactImpulse impulse){

    }







}
