package edu.chalmers.zombie.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.adapter.Book;
import edu.chalmers.zombie.adapter.Zombie;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;

/** A custom contact listener
 * Created by Erik on 2015-04-18.
 */
public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
    private GameModel gameModel;
    private MapController mapController;

    public ContactListener (){
        this.gameModel = GameModel.getInstance();
        this.mapController = new MapController();
    }
    /**
     * Decides what to do when two objects start colliding
     * @param contact   The contact between two objects
     */
    public void beginContact (Contact contact){     //Anropas när två saker börjar kollidera
        gameModel.clearEntitiesToRemove();
        if(contact.getFixtureB().getFilterData().categoryBits == Constants.COLLISION_PROJECTILE) {      //Är en bok
            if(contact.getFixtureA().getFilterData().categoryBits == Constants.COLLISION_PLAYER) {
                Book b = (Book) contact.getFixtureB().getBody().getUserData(); //TODO detta måste göras snyggare. Kanske en projectile-huvudklass?
                gameModel.addEntityToRemove(b);
                b.markForRemoval();
                gameModel.getPlayer().increaseAmmunition();
            }
            else if(contact.getFixtureA().getFilterData().categoryBits == Constants.COLLISION_ZOMBIE) {
                Book b = (Book) contact.getFixtureB().getBody().getUserData(); //TODO detta måste göras snyggare. Kanske en projectile-huvudklass?
                gameModel.addEntityToRemove(b);
                b.markForRemoval();
                Zombie z = (Zombie) contact.getFixtureA().getBody().getUserData();
                z.knockOut();
                gameModel.addEntityToRemove(z);
                z.setSprite(new Sprite(new Texture("core/assets/zombie_test_sleep.png")));      //TODO temp
                z.scaleSprite(1f/Constants.TILE_SIZE);
            }
            else if(contact.getFixtureA().getFilterData().categoryBits == Constants.COLLISION_OBSTACLE) {  //Boken har kolliderat med vägg eller liknande
                Book b = (Book) contact.getFixtureB().getBody().getUserData(); //TODO detta måste göras snyggare. Kanske en projectile-huvudklass?
                b.applyFriction();
            }
        }
        else if(contact.getFixtureB().getFilterData().categoryBits == Constants.COLLISION_PLAYER) {      //Är spelaren
            if(contact.getFixtureA().getFilterData().categoryBits == Constants.COLLISION_DOOR_PREVIOUS) {
                System.out.println("lol");
                mapController.loadNextLevel();

            }
            else   if(contact.getFixtureB().getFilterData().categoryBits == Constants.COLLISION_DOOR_PREVIOUS) {
                System.out.println("lolno");
                mapController.loadNextLevel();

            }
            else if(contact.getFixtureA().getFilterData().categoryBits == Constants.COLLISION_DOOR_PREVIOUS) {
                Book b = (Book) contact.getFixtureB().getBody().getUserData(); //TODO detta måste göras snyggare. Kanske en projectile-huvudklass?
                gameModel.addEntityToRemove(b);
                b.markForRemoval();
                Zombie z = (Zombie) contact.getFixtureA().getBody().getUserData();
                z.knockOut();
                gameModel.addEntityToRemove(z);
                z.setSprite(new Sprite(new Texture("core/assets/zombie_test_sleep.png")));      //TODO temp
                z.scaleSprite(1f/Constants.TILE_SIZE);
            }
            else if(contact.getFixtureA().getFilterData().categoryBits == Constants.COLLISION_OBSTACLE) {  //Boken har kolliderat med vägg eller liknande
                Book b = (Book) contact.getFixtureB().getBody().getUserData(); //TODO detta måste göras snyggare. Kanske en projectile-huvudklass?
                b.applyFriction();
            }
        }
    }

    public void endContact (Contact contact){       //Anropas när de inte längre kolliderar
     }

    public  void preSolve(Contact contact, Manifold manifold){

    }
    public  void postSolve(Contact contact,ContactImpulse impulse){

    }


}
