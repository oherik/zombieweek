package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * A zeta zombie has a low speed and a high resilience. It causes a damage of 100 percent.
 * It has a detection radius of 100, which is high.
 *
 * Created by neda on 2015-05-19.
 */
public class ZetaZombie extends Zombie {

    public ZetaZombie(ZWWorld world, int x, int y) {

        super(GameModel.getInstance().res.getTexture("zombie"),
                GameModel.getInstance().res.getTexture("zombie-still"),
                GameModel.getInstance().res.getTexture("zombie-dead"), world, x, y,32);
        setType(ZombieType.ZETA);
        setDetectionRadius(100);
        setStartingHp(250);
        setSpeed(30);
        setAngularSpeed(30);
        setDamage(100);
    }

    @Override
    public ZWVector getVelocity() {
        return null;
    }
}
