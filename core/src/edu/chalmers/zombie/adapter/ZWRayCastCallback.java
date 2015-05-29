package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

/**
 * Created by daniel on 5/29/2015.
 */
public abstract class ZWRayCastCallback implements RayCastCallback{


    public abstract float reportRayFixture(ZWFixture fixture, ZWVector point, ZWVector normal, float fraction);

    public float reportRayFixture(Fixture fixture, Vector2 point1, Vector2 point2, float fraction){
        return reportRayFixture(new ZWFixture(fixture), new ZWVector(point1), new ZWVector(point2), fraction);
    }
}
