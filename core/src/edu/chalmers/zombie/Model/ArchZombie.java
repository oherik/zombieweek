package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * An arch zombie has a high speed and a high resilience. It causes a damage of 200 percent.
 * It has a detection radius of 100, which is high.
 *
 * Created by neda on 2015-05-19.
 */
public class ArchZombie extends Zombie {

    public ArchZombie(ZWWorld world, int x, int y) {

        super(GameModel.getInstance().res.getTexture("zombie"),
                GameModel.getInstance().res.getTexture("zombie-still"),
                GameModel.getInstance().res.getTexture("zombie-dead"), world, x, y,32);
        setType(ZombieType.ARCH);
        setDetectionRadius(100);
        setStartingHp(500);
        setSpeed(800);
        setDamage(200);
    }

    @Override
    public ZWVector getVelocity() {
        return null;
    }
}
