package edu.chalmers.zombie.utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/** A custom contact listener
 * Created by Erik on 2015-04-18.
 */
public class ZWContactListener  implements ContactListener{

    public void beginContact (Contact contact){     //Anropas när två saker börjar kollidera
        System.out.println("Kollision");
    }

    public void endContact (Contact contact){       //Anropas när de inte längre kolliderar

    }

    public  void preSolve(Contact contact, Manifold manifold){

    }
    public  void postSolve(Contact contact,ContactImpulse impulse){

    }


}
