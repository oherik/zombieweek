package edu.chalmers.zombie.controller;

import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.model.Book;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/** A custom contact listener
 * Created by Erik on 2015-04-18.
 */
public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {

    /**
     * Decides what to do when two objects start colliding
     * @param contact   The contact between two objects
     */
    public void beginContact (Contact contact){     //Anropas när två saker börjar kollidera
        GameModel gameModel = GameModel.getInstance();
        gameModel.clearBodiesToRemove();
        if(contact.getFixtureB().getFilterData().categoryBits == Constants.COLLISION_PROJECTILE) {
            if(contact.getFixtureA().getFilterData().categoryBits == Constants.COLLISION_PLAYER) {
                Book b = (Book) contact.getFixtureB().getBody().getUserData(); //TODO detta måste göras snyggare. Kanske en projectile-huvudklass?
                gameModel.addBodyToRemove(contact.getFixtureB().getBody());
                b.markForRemoval();
                gameModel.getPlayer().increaseAmmunition();
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
