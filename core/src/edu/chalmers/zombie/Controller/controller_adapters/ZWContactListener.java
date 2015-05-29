package edu.chalmers.zombie.controller.controller_adapters;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import edu.chalmers.zombie.adapter.ZWFixture;
import edu.chalmers.zombie.controller.ContactController;

/**
 * Created by Erik on 2015-05-29.
 */
public class ZWContactListener  implements com.badlogic.gdx.physics.box2d.ContactListener  {
    /**
     * Called when two fixtures begin to touch.
     *
     * @param contact
     */
    @Override
    public void beginContact(Contact contact) {
        ZWFixture fixtureA = new ZWFixture(contact.getFixtureA());
        ZWFixture fixtureB = new ZWFixture(contact.getFixtureB());
        ContactController.beginContact(fixtureA,fixtureB);
    }

    /**
     * Called when two fixtures cease to touch.
     *
     * @param contact
     */
    @Override
    public void endContact(Contact contact) {
        ZWFixture fixtureA = new ZWFixture(contact.getFixtureA());
        ZWFixture fixtureB = new ZWFixture(contact.getFixtureB());
        ContactController.endContact(fixtureA, fixtureB);

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        ZWFixture fixtureA = new ZWFixture(contact.getFixtureA());
        ZWFixture fixtureB = new ZWFixture(contact.getFixtureB());
        ContactController.preSolve(fixtureA, fixtureB);

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
