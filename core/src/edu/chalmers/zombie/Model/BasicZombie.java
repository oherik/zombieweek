package edu.chalmers.zombie.model;

import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * A basic zombie has a fairly low resilience and low speed. It causes a damage of 20 percent,
 * which is measured as an hp loss of 2 in the GUI. It has a detection radius of 10,
 * which is intermediate.
 *
 * Created by neda on 2015-05-19.
 * * Modified by Erik
 */
public class BasicZombie extends Zombie {

    public BasicZombie(ZWWorld world, int x, int y) {

        super(GameModel.getInstance().res.getTexture("zombie"),
                GameModel.getInstance().res.getTexture("zombie-still"),
                GameModel.getInstance().res.getTexture("zombie-dead"),world,x,y,32);
        setType(ZombieType.BASIC);
        setDetectionRadius(10);
        setStartingHp(50);
        setSpeed(50);
        setAngularSpeed(100);
        setDamage(20);
    }

    @Override
    public ZWVector getVelocity() {
        return null;
    }
}
