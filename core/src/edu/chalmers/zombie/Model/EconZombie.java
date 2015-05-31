package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * An econ zombie - or ballerina zombie - has an intermediate resilience, but high speed. I
 * It causes a damage of 75 percent.It has a detection radius of 10, which is intermediate,
 * and spins towards the player when having detected them.
 *
 * Created by neda on 2015-05-19.
 */
public class EconZombie extends Zombie {

    public EconZombie(ZWWorld world, int x, int y) {

        super(GameModel.getInstance().res.getTexture("zombie"),
                GameModel.getInstance().res.getTexture("zombie-still"),
                GameModel.getInstance().res.getTexture("zombie-dead"), world, x, y,32);
        setType(ZombieType.ECON);
        setDetectionRadius(10);
        setStartingHp(50);
        setSpeed(200);
        setAngularSpeed(200);
        super.setAngularVelocity(10);
        setDamage(75);
    }

    @Override
    public ZWVector getVelocity() {
        return null;
    }
}
