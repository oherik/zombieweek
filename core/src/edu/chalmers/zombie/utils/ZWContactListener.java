package edu.chalmers.zombie.utils;

import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.model.Book;
import edu.chalmers.zombie.model.GameModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/** A custom contact listener
 * Created by Erik on 2015-04-18.
 */
public class ZWContactListener  implements ContactListener{


    private GameModel gameModel;

    public void beginContact (Contact contact){     //Anropas när två saker börjar kollidera
        this.gameModel = GameModel.getInstance();
        gameModel.clearBodiesToRemove();
        if(contact.getFixtureB().getFilterData().categoryBits == Constants.COLLISION_PROJECTILE) {
            System.out.println("bok");
            if(contact.getFixtureA().getFilterData().categoryBits == Constants.COLLISION_PLAYER) {
                Book b = (Book) contact.getFixtureB().getBody().getUserData(); //TODO detta måste göras snyggare. Kanske en projectile-huvudklass?
                gameModel.addBodyToRemove(contact.getFixtureB().getBody());
                b.markForRemoval();
                gameModel.getPlayer().addBook();
            }
        }
            System.out.println("Kollision");
    }

    public void endContact (Contact contact){       //Anropas när de inte längre kolliderar
     }

    public  void preSolve(Contact contact, Manifold manifold){

    }
    public  void postSolve(Contact contact,ContactImpulse impulse){

    }


}
